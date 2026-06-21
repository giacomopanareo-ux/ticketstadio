package it.unife.ticketstadio.service;
import it.unife.ticketstadio.dto.*;
import it.unife.ticketstadio.entity.*;
import it.unife.ticketstadio.exception.*;
import it.unife.ticketstadio.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.*;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class BigliettoServiceTest {
    @Mock private BigliettoRepository bigliettoRepo;
    @Mock private PartitaRepository partitaRepo;
    @Mock private PostoRepository postoRepo;
    @Mock private PromozioneRepository promoRepo;
    @Mock private PagamentoRepository pagamentoRepo;
    @Mock private UtenteRepository utenteRepo;
    @InjectMocks private BigliettoService service;
    private Utente utente; private Partita partita; private Posto posto; private Settore settore;
    @BeforeEach void setUp(){
        Stadio st=Stadio.builder().id(1L).nome("S").citta("C").capienzaTotale(100).build();
        settore=Settore.builder().id(1L).nome("CN").capienza(50).prezzoBase(new BigDecimal("20.00")).stadio(st).build();
        posto=Posto.builder().id(1L).fila("A").numero(1).settore(settore).build();
        Squadra c=Squadra.builder().id(1L).nome("SPAL").citta("FE").build();
        Squadra o=Squadra.builder().id(2L).nome("Bologna").citta("BO").build();
        partita=Partita.builder().id(1L).squadraCasa(c).squadraOspite(o).dataOra(LocalDateTime.now().plusDays(5)).stadio(st).stato(Partita.Stato.PROGRAMMATA).build();
        utente=Utente.builder().id(1L).nome("M").cognome("R").email("m@t.it").passwordHash("h").ruolo(Utente.Ruolo.TIFOSO).dataRegistrazione(LocalDate.now()).build();
    }
    private AcquistoBigliettoRequest req(String promo){AcquistoBigliettoRequest r=new AcquistoBigliettoRequest();r.setPartitaId(1L);r.setPostoId(1L);r.setCodicePromo(promo);r.setMetodoPagamento("CARTA");return r;}
    @Test @DisplayName("acquista senza promo → prezzo base")
    void acquista_noPromo(){
        when(utenteRepo.findByEmail("m@t.it")).thenReturn(Optional.of(utente));
        when(partitaRepo.findById(1L)).thenReturn(Optional.of(partita));
        when(postoRepo.findById(1L)).thenReturn(Optional.of(posto));
        when(bigliettoRepo.existsByPartitaAndPosto(any(),any())).thenReturn(false);
        when(bigliettoRepo.save(any())).thenAnswer(i->{Biglietto b=i.getArgument(0);b=Biglietto.builder().id(1L).partita(partita).posto(posto).utente(utente).prezzoPagato(b.getPrezzoPagato()).dataAcquisto(LocalDateTime.now()).stato(Biglietto.Stato.VALIDO).build();return b;});
        when(pagamentoRepo.save(any())).thenAnswer(i->i.getArgument(0));
        BigliettoResponse r=service.acquista(req(null),"m@t.it");
        assertThat(r.getPrezzoPagato()).isEqualByComparingTo("20.00");
    }
    @Test @DisplayName("acquista con promo 10% → sconto applicato")
    void acquista_promoSconto(){
        Promozione p=Promozione.builder().id(1L).codice("P10").scontoPercentuale(new BigDecimal("10")).dataInizio(LocalDate.now().minusDays(1)).dataFine(LocalDate.now().plusDays(30)).build();
        when(utenteRepo.findByEmail("m@t.it")).thenReturn(Optional.of(utente));
        when(partitaRepo.findById(1L)).thenReturn(Optional.of(partita));
        when(postoRepo.findById(1L)).thenReturn(Optional.of(posto));
        when(bigliettoRepo.existsByPartitaAndPosto(any(),any())).thenReturn(false);
        when(promoRepo.findByCodice("P10")).thenReturn(Optional.of(p));
        when(bigliettoRepo.save(any())).thenAnswer(i->{Biglietto b=i.getArgument(0);return Biglietto.builder().id(1L).partita(partita).posto(posto).utente(utente).prezzoPagato(b.getPrezzoPagato()).dataAcquisto(LocalDateTime.now()).stato(Biglietto.Stato.VALIDO).build();});
        when(pagamentoRepo.save(any())).thenAnswer(i->i.getArgument(0));
        BigliettoResponse r=service.acquista(req("P10"),"m@t.it");
        assertThat(r.getPrezzoPagato()).isEqualByComparingTo("18.00");
    }
    @Test @DisplayName("posto occupato → PostoOccupatoException")
    void acquista_postoOccupato(){
        when(utenteRepo.findByEmail("m@t.it")).thenReturn(Optional.of(utente));
        when(partitaRepo.findById(1L)).thenReturn(Optional.of(partita));
        when(postoRepo.findById(1L)).thenReturn(Optional.of(posto));
        when(bigliettoRepo.existsByPartitaAndPosto(any(),any())).thenReturn(true);
        assertThatThrownBy(()->service.acquista(req(null),"m@t.it")).isInstanceOf(PostoOccupatoException.class);
        verify(bigliettoRepo,never()).save(any());
    }
    @Test @DisplayName("partita annullata → IllegalStateException")
    void acquista_partitaAnnullata(){
        partita.setStato(Partita.Stato.ANNULLATA);
        when(utenteRepo.findByEmail("m@t.it")).thenReturn(Optional.of(utente));
        when(partitaRepo.findById(1L)).thenReturn(Optional.of(partita));
        when(postoRepo.findById(1L)).thenReturn(Optional.of(posto));
        assertThatThrownBy(()->service.acquista(req(null),"m@t.it")).isInstanceOf(IllegalStateException.class);
    }
    @Test @DisplayName("promo scaduta → IllegalArgumentException")
    void acquista_promoScaduta(){
        Promozione p=Promozione.builder().id(2L).codice("OLD").scontoPercentuale(new BigDecimal("10")).dataInizio(LocalDate.now().minusDays(30)).dataFine(LocalDate.now().minusDays(1)).build();
        when(utenteRepo.findByEmail("m@t.it")).thenReturn(Optional.of(utente));
        when(partitaRepo.findById(1L)).thenReturn(Optional.of(partita));
        when(postoRepo.findById(1L)).thenReturn(Optional.of(posto));
        when(bigliettoRepo.existsByPartitaAndPosto(any(),any())).thenReturn(false);
        when(promoRepo.findByCodice("OLD")).thenReturn(Optional.of(p));
        assertThatThrownBy(()->service.acquista(req("OLD"),"m@t.it")).isInstanceOf(IllegalArgumentException.class).hasMessageContaining("non applicabile");
    }
}
