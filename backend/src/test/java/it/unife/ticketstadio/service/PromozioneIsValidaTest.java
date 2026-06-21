package it.unife.ticketstadio.service;
import it.unife.ticketstadio.entity.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;
class PromozioneIsValidaTest {
    private Promozione p(LocalDate i,LocalDate f,Partita pt){return Promozione.builder().id(1L).codice("T").scontoPercentuale(new BigDecimal("10")).dataInizio(i).dataFine(f).partita(pt).build();}
    private Partita pt(Long id){return Partita.builder().id(id).stato(Partita.Stato.PROGRAMMATA).build();}
    @Test @DisplayName("promo generica nel periodo → true") void generica_valida(){assertThat(p(LocalDate.now().minusDays(1),LocalDate.now().plusDays(10),null).isValida(pt(1L))).isTrue();}
    @Test @DisplayName("promo scaduta → false") void scaduta(){assertThat(p(LocalDate.now().minusDays(10),LocalDate.now().minusDays(1),null).isValida(pt(1L))).isFalse();}
    @Test @DisplayName("promo futura → false") void futura(){assertThat(p(LocalDate.now().plusDays(1),LocalDate.now().plusDays(10),null).isValida(pt(1L))).isFalse();}
    @Test @DisplayName("promo per partita giusta → true") void partitaGiusta(){Partita x=pt(5L);assertThat(p(LocalDate.now().minusDays(1),LocalDate.now().plusDays(5),x).isValida(x)).isTrue();}
    @Test @DisplayName("promo per altra partita → false") void altraPartita(){assertThat(p(LocalDate.now().minusDays(1),LocalDate.now().plusDays(5),pt(5L)).isValida(pt(99L))).isFalse();}
    @Test @DisplayName("promo scade oggi → true") void scadeOggi(){assertThat(p(LocalDate.now().minusDays(5),LocalDate.now(),null).isValida(pt(1L))).isTrue();}
}
