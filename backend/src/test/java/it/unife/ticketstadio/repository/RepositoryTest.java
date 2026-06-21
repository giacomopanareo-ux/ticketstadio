package it.unife.ticketstadio.repository;
import it.unife.ticketstadio.entity.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.*;
import static org.assertj.core.api.Assertions.*;
@DataJpaTest @ActiveProfiles("test")
class RepositoryTest {
    @Autowired private UtenteRepository utenteRepo;
    @Autowired private StadioRepository stadioRepo;
    @Autowired private SettoreRepository settoreRepo;
    @Autowired private PostoRepository postoRepo;
    @Autowired private SquadraRepository squadraRepo;
    @Autowired private PartitaRepository partitaRepo;
    @Autowired private BigliettoRepository bigliettoRepo;
    @Autowired private PromozioneRepository promoRepo;
    private Stadio stadio; private Settore settore; private Posto posto1,posto2; private Partita partita; private Utente utente;
    @BeforeEach void setUp(){
        stadio=stadioRepo.save(Stadio.builder().nome("S").citta("C").capienzaTotale(100).build());
        settore=settoreRepo.save(Settore.builder().nome("CN").capienza(50).prezzoBase(new BigDecimal("15")).stadio(stadio).build());
        posto1=postoRepo.save(Posto.builder().fila("A").numero(1).settore(settore).build());
        posto2=postoRepo.save(Posto.builder().fila("A").numero(2).settore(settore).build());
        Squadra c=squadraRepo.save(Squadra.builder().nome("SPAL").citta("FE").build());
        Squadra o=squadraRepo.save(Squadra.builder().nome("Bologna").citta("BO").build());
        partita=partitaRepo.save(Partita.builder().squadraCasa(c).squadraOspite(o).dataOra(LocalDateTime.now().plusDays(3)).stadio(stadio).stato(Partita.Stato.PROGRAMMATA).build());
        utente=utenteRepo.save(Utente.builder().nome("M").cognome("R").email("m@t.it").passwordHash("h").ruolo(Utente.Ruolo.TIFOSO).dataRegistrazione(LocalDate.now()).build());
    }
    @Test @DisplayName("findByEmail trovato") void findByEmail_found(){assertThat(utenteRepo.findByEmail("m@t.it")).isPresent();}
    @Test @DisplayName("findByEmail non trovato") void findByEmail_notFound(){assertThat(utenteRepo.findByEmail("x@t.it")).isEmpty();}
    @Test @DisplayName("existsByEmail true") void existsByEmail_true(){assertThat(utenteRepo.existsByEmail("m@t.it")).isTrue();}
    @Test @DisplayName("posto libero → false") void posto_libero(){assertThat(bigliettoRepo.existsByPartitaAndPosto(partita,posto1)).isFalse();}
    @Test @DisplayName("posto dopo acquisto → true") void posto_occupato(){
        bigliettoRepo.save(Biglietto.builder().partita(partita).posto(posto1).utente(utente).prezzoPagato(new BigDecimal("15")).dataAcquisto(LocalDateTime.now()).stato(Biglietto.Stato.VALIDO).build());
        assertThat(bigliettoRepo.existsByPartitaAndPosto(partita,posto1)).isTrue();
    }
    @Test @DisplayName("findPostiOccupati → solo posti acquistati") void postiOccupati(){
        bigliettoRepo.save(Biglietto.builder().partita(partita).posto(posto1).utente(utente).prezzoPagato(new BigDecimal("15")).dataAcquisto(LocalDateTime.now()).stato(Biglietto.Stato.VALIDO).build());
        var occ=bigliettoRepo.findPostiOccupatiByPartita(partita.getId());
        assertThat(occ).contains(posto1.getId()).doesNotContain(posto2.getId());
    }
    @Test @DisplayName("findByCodice trovato") void findByCodice_found(){
        promoRepo.save(Promozione.builder().codice("P10").scontoPercentuale(new BigDecimal("10")).dataInizio(LocalDate.now().minusDays(1)).dataFine(LocalDate.now().plusDays(30)).build());
        assertThat(promoRepo.findByCodice("P10")).isPresent();
    }
}
