const API_BASE='http://localhost:8080/api';
async function apiFetch(endpoint,options={}){
    const token=localStorage.getItem('jwt_token');
    const headers={'Content-Type':'application/json',...(token?{'Authorization':`Bearer ${token}`}:{}),...(options.headers||{})};
    const response=await fetch(`${API_BASE}${endpoint}`,{...options,headers});
    if(response.status===401){localStorage.removeItem('jwt_token');window.location.href='/login.html';return;}
    if(!response.ok){const err=await response.json().catch(()=>({error:'Errore sconosciuto'}));throw new Error(err.error||`Errore HTTP ${response.status}`);}
    if(response.status===204)return null;
    return response.json();
}
const PartiteAPI={getAll:()=>apiFetch('/partite'),getById:id=>apiFetch(`/partite/${id}`),getPosti:id=>apiFetch(`/partite/${id}/posti`),crea:d=>apiFetch('/partite',{method:'POST',body:JSON.stringify(d)}),aggiorna:(id,d)=>apiFetch(`/partite/${id}`,{method:'PUT',body:JSON.stringify(d)}),annulla:id=>apiFetch(`/partite/${id}/annulla`,{method:'PUT'})};
const BigliettiAPI={acquista:d=>apiFetch('/biglietti/acquista',{method:'POST',body:JSON.stringify(d)}),getMiei:()=>apiFetch('/biglietti/miei'),getById:id=>apiFetch(`/biglietti/${id}`)};
const AbbonamentiAPI={getMiei:()=>apiFetch('/abbonamenti/miei'),sottoscrivi:d=>apiFetch('/abbonamenti',{method:'POST',body:JSON.stringify(d)}),rinnova:id=>apiFetch(`/abbonamenti/${id}/rinnova`,{method:'PUT'})};
const PromozioniAPI={valida:d=>apiFetch('/promozioni/valida',{method:'POST',body:JSON.stringify(d)}),getAll:()=>apiFetch('/promozioni'),crea:d=>apiFetch('/promozioni',{method:'POST',body:JSON.stringify(d)}),disattiva:id=>apiFetch(`/promozioni/${id}/disattiva`,{method:'PUT'})};
const SettoriAPI={getAll:()=>apiFetch('/settori'),getPosti:id=>apiFetch(`/settori/${id}/posti`),crea:d=>apiFetch('/settori',{method:'POST',body:JSON.stringify(d)}),aggiorna:(id,d)=>apiFetch(`/settori/${id}`,{method:'PUT',body:JSON.stringify(d)})};
const AdminAPI={getStatisticheVendite:()=>apiFetch('/admin/statistiche/vendite'),getStatistichePartita:id=>apiFetch(`/admin/statistiche/partita/${id}`)};
