package it.unife.ticketstadio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.repository.UtenteRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test di integrazione del flusso di autenticazione (registrazione + login).
 *
 * - @SpringBootTest      -> avvia l'intero contesto Spring (test "end-to-end" sul backend).
 * - @AutoConfigureMockMvc -> fornisce MockMvc per simulare chiamate HTTP senza un server reale.
 * - @ActiveProfiles("test") -> usa la configurazione di test (tipicamente un DB in memoria).
 * - @TestMethodOrder(...) -> i test vengono eseguiti nell'ordine indicato da @Order,
 *   perché condividono lo stesso utente (prima lo si registra, poi si fa login).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired private MockMvc mvc;          // simula le richieste HTTP
    @Autowired private ObjectMapper mapper;  // converte gli oggetti in JSON
    @Autowired private UtenteRepository utenteRepo;

    private static final String EMAIL = "mario.rossi@test.it", PASSWORD = "Password123!";

    @Test
    @Order(1)
    @DisplayName("POST /api/auth/register → 201 con dati validi")
    void register_valid() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setNome("Mario");
        req.setCognome("Rossi");
        req.setEmail(EMAIL);
        req.setPassword(PASSWORD);

        // Mi aspetto 201 Created e che l'utente risulti effettivamente salvato.
        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        assertTrue(utenteRepo.existsByEmail(EMAIL));
    }

    @Test
    @Order(2)
    @DisplayName("POST /api/auth/register → 400 email duplicata")
    void register_duplicate() throws Exception {
        // Stessa email del test precedente: deve fallire con 400 e messaggio dedicato.
        RegisterRequest req = new RegisterRequest();
        req.setNome("X");
        req.setCognome("Y");
        req.setEmail(EMAIL);
        req.setPassword(PASSWORD);

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("già registrata")));
    }

    @Test
    @Order(3)
    @DisplayName("POST /api/auth/register → 400 password corta")
    void register_shortPwd() throws Exception {
        // Password troppo corta: viene bloccata dalla validazione (@Size(min=6)).
        RegisterRequest req = new RegisterRequest();
        req.setNome("X");
        req.setCognome("Y");
        req.setEmail("new@test.it");
        req.setPassword("123");

        mvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(4)
    @DisplayName("POST /api/auth/login → 200 con JWT")
    void login_valid() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(EMAIL);
        req.setPassword(PASSWORD);

        MvcResult r = mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        // Un JWT è composto da 3 parti separate da punti (header.payload.firma).
        String token = mapper.readTree(r.getResponse().getContentAsString()).get("token").asText();
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    @Order(5)
    @DisplayName("POST /api/auth/login → 401 password errata")
    void login_wrongPwd() throws Exception {
        LoginRequest req = new LoginRequest();
        req.setEmail(EMAIL);
        req.setPassword("Sbagliata!"); // password non corretta

        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
