# Miglioramenti al codice — checklist criticità

> Questa guida copre i 3 criteri di valutazione che si massimizzano *prima* dell'esame con piccoli interventi sul codice esistente:
> - **Criterio 6** — Presenza e qualità dei commenti
> - **Criterio 9** — Accessibilità e usabilità

---

## A. COMMENTI NEL CODICE (criterio 6)

> **Regola d'oro:** commenti **brevi e di intento**, non descrittivi. Non scrivere "questo metodo restituisce X" se il nome del metodo è già `getX()`. Spiega **perché**, non **cosa**.

### A.1 — JavaDoc sulle classi pubbliche

Aggiungi su ogni classe `@Service`, `@Controller`, `@Configuration` un JavaDoc di 2-3 righe:

```java
/**
 * Service che gestisce l'acquisto biglietti, l'applicazione delle promozioni
 * e la creazione del pagamento associato. Tutti i metodi sono transazionali.
 *
 * @author Giacomo Panareo
 */
@Service
@RequiredArgsConstructor
public class BigliettoService {
    ...
}
```

**File da rivedere assolutamente:**
- Tutti i controller in `controller/`
- Tutti i service in `service/`
- Tutte le entity in `entity/` (descrivi il dominio)
- `JwtUtil`, `JwtFilter`, `SecurityConfig` (qui i commenti contano doppio perché sono codice "non ovvio")

### A.2 — Commenti inline sui passaggi critici

Identifica i **punti delicati** dove il "perché" non è ovvio dal codice. Esempi reali nel tuo progetto:

#### In `BigliettoService.acquista()`:
```java
@Transactional
public BigliettoResponse acquista(AcquistoBigliettoRequest req, String email) {
    // Recupero atomico delle entità: se una non esiste, la transazione
    // viene rollback prima di fare modifiche
    Utente utente = utenteRepo.findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("Utente"));
    Partita partita = partitaRepo.findById(req.getPartitaId())
        .orElseThrow(() -> new ResourceNotFoundException("Partita"));
    Posto posto = postoRepo.findById(req.getPostoId())
        .orElseThrow(() -> new ResourceNotFoundException("Posto"));

    // Controllo applicativo: previene 90% delle richieste su posti occupati
    // senza scomodare il DB. La vera garanzia atomica è il vincolo UNIQUE
    // (partita_id, posto_id) che blocca anche le race condition concorrenti.
    if (bigliettoRepo.existsByPartitaAndPosto(partita, posto))
        throw new PostoOccupatoException("Posto già occupato per questa partita");

    // ... etc
}
```

#### In `JwtFilter.doFilterInternal()`:
```java
@Override
protected void doFilterInternal(...) throws ServletException, IOException {
    String auth = req.getHeader("Authorization");

    // Pass-through se non c'è header Bearer: gli endpoint pubblici
    // (login, register, GET partite) devono funzionare senza token
    if (auth == null || !auth.startsWith("Bearer ")) {
        chain.doFilter(req, res);
        return;
    }

    String token = auth.substring(7); // "Bearer ".length()
    ...
}
```

#### In `PromozioneService.crea()`:
```java
public Promozione crea(PromozioneRequest req) {
    // Codice promo case-insensitive: salviamo in maiuscolo e confrontiamo
    // così, per evitare che WELCOME10 e welcome10 siano due promozioni
    if (promoRepo.findByCodice(req.getCodice().toUpperCase()).isPresent())
        throw new IllegalArgumentException("Codice già esistente");

    // Coerenza temporale verificata applicativamente (esiste anche il
    // CHECK al DB ma fallirebbe con un errore meno chiaro per l'utente)
    if (req.getDataFine().isBefore(req.getDataInizio()))
        throw new IllegalArgumentException("Data fine precedente a data inizio");
    ...
}
```

### A.3 — Commenti sulle annotazioni JPA critiche

Sulle entità `Biglietto`, `Abbonamento`, `Promozione` aggiungi commenti che spiegano le scelte di vincolo:

```java
@Entity
@Table(name = "biglietto",
       // Vincolo core del dominio: impedisce a livello DB la doppia vendita
       // dello stesso posto per la stessa partita. Anche con concorrenza,
       // la seconda INSERT viene rifiutata con violazione di constraint.
       uniqueConstraints = {@UniqueConstraint(columnNames = {"partita_id", "posto_id"})})
public class Biglietto { ... }
```

### A.4 — Cosa NON commentare

❌ Non scrivere commenti che ripetono il codice:
```java
// INCREMENTA i di 1
i++;
```

❌ Non scrivere commenti banali:
```java
// Restituisce l'id
public Long getId() { return id; }
```

❌ Non scrivere TODO che non hai intenzione di risolvere:
```java
// TODO: gestire questo caso meglio
return null;
```

### A.5 — Commenti nel frontend

In JavaScript, JSDoc sulle funzioni esportate "pubbliche":

```javascript
/**
 * Wrapper sopra fetch() che aggiunge automaticamente il token JWT
 * dall'localStorage, gestisce gli errori 401 (token scaduto → redirect)
 * e restituisce direttamente il JSON parsed.
 *
 * @param {string} endpoint - Path relativo, es. "/partite"
 * @param {object} options  - Opzioni fetch standard (method, body, ...)
 * @returns {Promise<any>}  - Body JSON deserializzato, o null per 204
 */
async function apiFetch(endpoint, options = {}) { ... }
```

---

## B. ACCESSIBILITÀ (criterio 9)

> Web Content Accessibility Guidelines (WCAG 2.1) Livello AA. Sono i 4 principi: **Percepibile, Operabile, Comprensibile, Robusto**.

### B.1 — HTML semantico

Controlla che il tuo HTML usi i tag corretti:

✅ **Corretto:**
```html
<header>
  <nav>
    <ul>
      <li><a href="/index.html">Partite</a></li>
    </ul>
  </nav>
</header>
<main>
  <h1>Prossime partite</h1>
  <section aria-labelledby="filtri-h">
    <h2 id="filtri-h">Filtra</h2>
    ...
  </section>
</main>
```

❌ **Da evitare:**
```html
<div class="header">
  <div class="nav">
    <div>Partite</div>
  </div>
</div>
```

### B.2 — Aria labels sui controlli

Ogni input deve avere un `<label>` collegato. Già lo fai per i form di login/registrazione, controlla che sia ovunque.

Per le icone-bottone (es. tasto X per chiudere modal):
```html
<button onclick="closeModal()" aria-label="Chiudi modal">
  <svg>...</svg>
</button>
```

### B.3 — Stati di errore accessibili

I messaggi di errore devono essere annunciati dai screen reader. Usa `aria-live`:

```html
<!-- nel toast -->
<div class="toast" role="alert" aria-live="assertive">
  Posto già occupato
</div>
```

Aggiorna in `utils.js` la funzione `showToast`:
```javascript
function showToast(message, type = 'info') {
  document.querySelectorAll('.toast').forEach(t => t.remove());
  const t = document.createElement('div');
  t.className = `toast toast-${type}`;
  t.setAttribute('role', 'alert');         // ← AGGIUNGI
  t.setAttribute('aria-live', 'assertive'); // ← AGGIUNGI
  t.textContent = message;
  document.body.appendChild(t);
  ...
}
```

### B.4 — Contrasto colore

Verifica con https://webaim.org/resources/contrastchecker/ che:
- Testo normale: ratio minimo **4.5:1**
- Testo grande (18pt+): ratio minimo **3:1**

Il tuo verde `#1A7A4A` su bianco è OK (ratio ~6.5:1). Il grigio chiaro `#9CA3AF` su bianco è **borderline** (~2.8:1) → usalo solo per testo decorativo, non per informazioni importanti.

### B.5 — Navigazione da tastiera

Tutti gli elementi interattivi devono essere raggiungibili con TAB e attivabili con ENTER/SPAZIO. Test rapido:
1. Apri la pagina, premi TAB ripetutamente
2. Verifica che il focus sia sempre visibile (focus ring)
3. Verifica che l'ordine di tabulazione sia logico (alto→basso, sinistra→destra)
4. Verifica che ENTER apra link/bottoni

Aggiungi nel CSS:
```css
*:focus-visible {
  outline: 3px solid var(--green);
  outline-offset: 2px;
}
```

### B.6 — Mappa posti accessibile

La griglia posti è una sfida per accessibilità. Mitiga con:
```html
<button class="posto-btn libero"
        aria-label="Posto A5, libero, prezzo 25 euro"
        aria-pressed="false"
        onclick="selectPosto(...)">
  A5
</button>
```
Quando selezionato, imposta `aria-pressed="true"`.

---

## C. USABILITÀ (criterio 9)

> "Quanto è facile da usare per un utente reale, alla prima esperienza"

### C.1 — Feedback istantaneo

Ogni azione deve dare feedback **entro 200ms**. Esempi:
- ✅ Click su Acquista → bottone si disabilita + cambia testo a "Acquisto…"
- ✅ Toast verde al success, rosso all'errore
- ✅ Loader durante caricamento lista partite

Verifica nel tuo codice che TUTTI i bottoni con azione async siano disabilitati durante il fetch:

```javascript
async function acquista() {
  const btn = document.getElementById('btn-acquista');
  btn.disabled = true;
  btn.textContent = 'Acquisto…';
  try {
    await BigliettiAPI.acquista(...);
    showToast('Biglietto acquistato!', 'success');
  } catch (err) {
    showToast(err.message, 'error');
    btn.disabled = false;          // ← non dimenticare
    btn.textContent = 'Acquista';   // ← in caso di errore!
  }
}
```

### C.2 — Conferme su azioni distruttive

Annullamento partita, disattivazione promo, rinnovo abbonamento devono chiedere conferma:
```javascript
if (!confirm('Annullare questa partita? I biglietti venduti verranno invalidati.')) return;
```

Già lo fai in `admin/partite.html`. **Verifica che ci sia anche su disattivazione promo e rinnovo abbonamento.**

### C.3 — Empty states informativi

Quando una lista è vuota, mostra un messaggio utile, non una pagina vuota:
```html
<div class="empty-state">
  <p>Non hai ancora acquistato biglietti.</p>
  <a href="/index.html" class="btn btn-primary">Sfoglia le partite</a>
</div>
```

### C.4 — Form: mostra errori vicino al campo

Già usi `showToast` ma per i form è meglio mostrare l'errore **sotto il campo**:
```html
<div class="form-group">
  <label>Email</label>
  <input type="email" id="email" aria-describedby="email-err">
  <p id="email-err" class="form-error" style="display:none">Email non valida</p>
</div>
```

### C.5 — Indicazione di sistema occupato

Se un'operazione dura più di 500ms, mostra un loader. Il tuo `showLoader(id)` esiste ma controlla che sia usato in tutti i caricamenti iniziali.

### C.6 — Breadcrumb e navigazione

Già hai un breadcrumb in `partita.html` ("Partite > X vs Y"). Controlla che sia consistente in tutte le pagine interne.

### C.7 — Mobile responsive

Apri il sito su dispositivo mobile (o DevTools → Responsive mode → iPhone). Verifica:
- [ ] La navbar collassa o nasconde i link secondari
- [ ] I bottoni hanno altezza minima 44px (touch target)
- [ ] La griglia posti scrolla orizzontalmente se necessario
- [ ] I modal sono leggibili su 375px di larghezza
- [ ] Non c'è scroll orizzontale indesiderato

---

## D. CHECKLIST DA SPUNTARE

Prima dell'esame, dedica 1-2 ore a queste piccole modifiche:

### Commenti (criterio 6)
- [ ] JavaDoc su tutti i service (8 classi)
- [ ] JavaDoc su tutti i controller (8 classi)
- [ ] Commenti inline sui 5 punti critici (acquista, login, validazione promo, annullamento partita, security filter)
- [ ] JSDoc su `apiFetch`, `getCurrentUser`, `showToast`

### Accessibilità (criterio 9 / 1)
- [ ] HTML semantico (`<header>`, `<main>`, `<section>`)
- [ ] `aria-label` su bottoni con sole icone
- [ ] `role="alert"` + `aria-live` sui toast
- [ ] Test navigazione TAB su 3 pagine principali
- [ ] CSS focus-visible

### Usabilità (criterio 9 / 2)
- [ ] Tutti i bottoni async disabilitati durante fetch
- [ ] Conferma su tutte le azioni distruttive
- [ ] Empty state in tutte le liste vuote
- [ ] Test su mobile (Responsive mode)

---

## E. COSA DIRE ALL'ESAME

Se il prof ti chiede *"come hai garantito l'accessibilità?"*, risposta tipo:

> "Ho seguito le WCAG 2.1 livello AA: HTML semantico con `<header>`, `<main>`, `<nav>`; tutti gli input hanno label associate; i toast hanno `role='alert'` per essere annunciati dai screen reader; il contrasto colore è verificato col WebAIM tool e supera il 4.5:1 per il testo normale; la navigazione da tastiera funziona ovunque con focus visibile. Lo testo apertamente non è perfetto: i punti più sfidanti sarebbero il selettore posti per chi usa solo tastiera e l'animazione del menu mobile per chi ha vestiboli sensibili. Per la prossima iterazione vorrei integrare axe DevTools nel CI."

Onestà > pretesa di perfezione.
