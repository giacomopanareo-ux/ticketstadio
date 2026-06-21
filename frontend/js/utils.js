function formatDate(d){if(!d)return'—';return new Date(d).toLocaleDateString('it-IT',{weekday:'long',day:'numeric',month:'long',year:'numeric'});}
function formatDateShort(d){if(!d)return'—';return new Date(d).toLocaleDateString('it-IT',{day:'2-digit',month:'2-digit',year:'numeric'});}
function formatTime(d){if(!d)return'—';return new Date(d).toLocaleTimeString('it-IT',{hour:'2-digit',minute:'2-digit'});}
function formatPrice(a){if(a==null)return'—';return new Intl.NumberFormat('it-IT',{style:'currency',currency:'EUR'}).format(a);}
function showToast(message,type='info'){document.querySelectorAll('.toast').forEach(t=>t.remove());const t=document.createElement('div');t.className=`toast toast-${type}`;t.textContent=message;document.body.appendChild(t);requestAnimationFrame(()=>t.classList.add('toast-visible'));setTimeout(()=>{t.classList.remove('toast-visible');setTimeout(()=>t.remove(),300);},3500);}
function statoPartitaBadge(s){const m={PROGRAMMATA:['programmata','badge-programmata'],IN_CORSO:['in corso','badge-in-corso'],CONCLUSA:['conclusa','badge-conclusa'],ANNULLATA:['annullata','badge-annullata']};const[l,c]=m[s]||[s.toLowerCase(),''];return`<span class="badge ${c}">${l}</span>`;}
function showLoader(id){const el=document.getElementById(id);if(el)el.innerHTML='<div class="loader-wrap"><div class="loader"></div></div>';}
function showError(id,msg){const el=document.getElementById(id);if(el)el.innerHTML=`<div class="empty-state"><p class="empty-icon">!</p><p>${msg}</p></div>`;}
function showEmpty(id,msg='Nessun elemento trovato'){const el=document.getElementById(id);if(el)el.innerHTML=`<div class="empty-state"><p class="empty-icon">○</p><p>${msg}</p></div>`;}
