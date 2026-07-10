function formatDate(d){if(!d)return'—';return new Date(d).toLocaleDateString('it-IT',{weekday:'long',day:'numeric',month:'long',year:'numeric'});}
function formatDateShort(d){if(!d)return'—';return new Date(d).toLocaleDateString('it-IT',{day:'2-digit',month:'2-digit',year:'numeric'});}
function formatTime(d){if(!d)return'—';return new Date(d).toLocaleTimeString('it-IT',{hour:'2-digit',minute:'2-digit'});}
function formatPrice(a){if(a==null)return'—';return new Intl.NumberFormat('it-IT',{style:'currency',currency:'EUR'}).format(a);}
function showToast(message,type='info'){document.querySelectorAll('.toast').forEach(t=>t.remove());const t=document.createElement('div');t.className=`toast toast-${type}`;t.textContent=message;document.body.appendChild(t);requestAnimationFrame(()=>t.classList.add('toast-visible'));setTimeout(()=>{t.classList.remove('toast-visible');setTimeout(()=>t.remove(),300);},3500);}
function statoPartitaBadge(s){const m={PROGRAMMATA:['programmata','badge-programmata'],IN_CORSO:['in corso','badge-in-corso'],CONCLUSA:['conclusa','badge-conclusa'],ANNULLATA:['annullata','badge-annullata']};const[l,c]=m[s]||[s.toLowerCase(),''];return`<span class="badge ${c}">${l}</span>`;}
function showLoader(id){const el=document.getElementById(id);if(el)el.innerHTML='<div class="loader-wrap"><div class="loader"></div></div>';}
function showError(id,msg){const el=document.getElementById(id);if(el)el.innerHTML=`<div class="empty-state"><p class="empty-icon">!</p><p>${msg}</p></div>`;}
function showEmpty(id,msg='Nessun elemento trovato'){const el=document.getElementById(id);if(el)el.innerHTML=`<div class="empty-state"><p class="empty-icon">○</p><p>${msg}</p></div>`;}
function confirmDialog(opts={}){
  const{title='Conferma',message='',confirmText='Conferma',cancelText='Annulla',danger=false}=opts;
  return new Promise(resolve=>{
    const ov=document.createElement('div');
    ov.className='confirm-overlay';
    ov.innerHTML='<div class="confirm-box" role="alertdialog" aria-modal="true"><div class="confirm-icon'+(danger?' danger':'')+'">'+(danger?'!':'?')+'</div><h3 class="confirm-title"></h3><p class="confirm-msg"></p><div class="confirm-actions"><button type="button" class="btn btn-outline confirm-cancel"></button><button type="button" class="btn '+(danger?'btn-danger':'btn-primary')+' confirm-ok"></button></div></div>';
    ov.querySelector('.confirm-title').textContent=title;
    ov.querySelector('.confirm-msg').textContent=message;
    const cancelBtn=ov.querySelector('.confirm-cancel');cancelBtn.textContent=cancelText;
    const okBtn=ov.querySelector('.confirm-ok');okBtn.textContent=confirmText;
    document.body.appendChild(ov);
    requestAnimationFrame(()=>ov.classList.add('open'));
    function close(val){ov.classList.remove('open');document.removeEventListener('keydown',onKey);setTimeout(()=>ov.remove(),200);resolve(val);}
    function onKey(e){if(e.key==='Escape')close(false);else if(e.key==='Enter')close(true);}
    ov.addEventListener('click',e=>{if(e.target===ov)close(false);});
    cancelBtn.addEventListener('click',()=>close(false));
    okBtn.addEventListener('click',()=>close(true));
    document.addEventListener('keydown',onKey);
    setTimeout(()=>okBtn.focus(),50);
  });
}
