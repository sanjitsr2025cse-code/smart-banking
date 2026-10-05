const API = '';
const state = { token: localStorage.getItem('sb_token'), userId: localStorage.getItem('sb_user_id'), user: null, accounts: [], transactions: [], modal: null };
const app = document.getElementById('app');

async function api(path, options = {}) {
  const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
  if (state.token) headers.Authorization = `Bearer ${state.token}`;
  const res = await fetch(API + path, { ...options, headers });
  const text = await res.text();
  let data = null; try { data = text ? JSON.parse(text) : null; } catch { data = text; }
  if (!res.ok) throw new Error(data?.message || data || `Request failed (${res.status})`);
  return data;
}

function esc(value) { return String(value ?? '').replace(/[&<>'"]/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;',"'":'&#39;','"':'&quot;'}[c])); }
function money(v) { return new Intl.NumberFormat('en-IN', { style: 'currency', currency: 'INR' }).format(Number(v || 0)); }
function date(v) { return v ? new Date(v).toLocaleString('en-IN', { dateStyle: 'medium', timeStyle: 'short' }) : '-'; }
function message(text, type='success') { return `<div class="notice ${type}">${esc(text)}</div>`; }

function authView(mode='login', notice='') {
  const login = mode === 'login';
  app.innerHTML = `<div class="auth-shell"><div class="auth-card">
    <div class="brand">SmartBank</div><p class="muted">Simple banking dashboard</p>
    ${notice}
    <h1>${login ? 'Welcome back' : 'Create account'}</h1>
    <p class="muted">${login ? 'Sign in to manage your accounts.' : 'Create your banking profile.'}</p>
    ${!login ? `<div class="field"><label>Name</label><input id="name" placeholder="Sanjit S R"></div>` : ''}
    <div class="field"><label>Email</label><input id="email" type="email" placeholder="you@example.com"></div>
    <div class="field"><label>Password</label><input id="password" type="password" placeholder="Minimum 8 characters"></div>
    <button class="btn full" onclick="${login ? 'login()' : 'register()'}">${login ? 'Sign in' : 'Create account'}</button>
    <p class="muted small" style="margin-top:18px;text-align:center">${login ? `New here? <span class="link" onclick="authView('register')">Create an account</span>` : `Already registered? <span class="link" onclick="authView('login')">Sign in</span>`}</p>
  </div></div>`;
}

async function register() {
  try {
    const data = await api('/users', { method:'POST', body: JSON.stringify({ name: document.getElementById('name').value, email: document.getElementById('email').value, password: document.getElementById('password').value }) });
    authView('login', message(`Account created for ${data.name}. Please sign in.`));
  } catch(e) { authView('register', message(e.message, 'error')); }
}
async function login() {
  try {
    const data = await api('/auth/login', { method:'POST', body: JSON.stringify({ email: document.getElementById('email').value, password: document.getElementById('password').value }) });
    state.token = data.token; state.userId = data.userId;
    localStorage.setItem('sb_token', state.token); localStorage.setItem('sb_user_id', state.userId);
    await dashboard();
  } catch(e) { authView('login', message(e.message, 'error')); }
}
function logout() { localStorage.clear(); state.token = null; state.userId = null; authView('login', message('You have been signed out.')); }

async function dashboard() {
  try {
    state.user = await api(`/users/${state.userId}`);
    state.accounts = await api(`/accounts/user/${state.userId}`);
    state.transactions = [];
    for (const a of state.accounts) { try { state.transactions.push(...await api(`/transactions/account/${a.id}`)); } catch {} }
    state.transactions.sort((a,b) => new Date(b.createdAt) - new Date(a.createdAt));
    renderDashboard();
  } catch(e) { logout(); authView('login', message(e.message, 'error')); }
}

function renderDashboard() {
  const total = state.accounts.reduce((s,a) => s + Number(a.balance || 0), 0);
  app.innerHTML = `<div class="shell"><header class="topbar"><div class="brand">SmartBank</div><div class="right"><span class="muted small">${esc(state.user?.name || '')}</span><button class="btn ghost" onclick="logout()">Logout</button></div></header>
  <main class="dashboard"><div class="hero"><div><h1>Dashboard</h1><p class="muted">Your accounts and recent activity in one place.</p></div><div style="display:flex;gap:8px"><button class="btn secondary" onclick="openTransferModal()">Transfer</button><button class="btn" onclick="openAccountModal()">+ New account</button></div></div>
  <div class="cards"><div class="card"><div class="stat-label">Total balance</div><div class="stat">${money(total)}</div></div><div class="card"><div class="stat-label">Accounts</div><div class="stat">${state.accounts.length}</div></div><div class="card"><div class="stat-label">Transactions</div><div class="stat">${state.transactions.length}</div></div></div>
  <div class="grid"><section class="card"><h2>My accounts</h2>${state.accounts.length ? state.accounts.map(accountHtml).join('') : `<p class="muted">No accounts yet. Create your first account.</p>`}</section>
  <section class="card"><h2>Recent transactions</h2>${transactionsHtml()}</section></div></main>${state.modal || ''}</div>`;
}
function accountHtml(a) { return `<div class="account"><div><div class="account-number">${esc(a.accountNumber)}</div><div class="muted small">${esc(a.accountType)}</div></div><div style="text-align:right"><div class="balance">${money(a.balance)}</div><div class="actions"><button class="btn secondary" onclick="openMoneyModal('deposit',${a.id})">Deposit</button><button class="btn secondary" onclick="openMoneyModal('withdraw',${a.id})">Withdraw</button><button class="btn ghost" onclick="showAccountTransactions(${a.id})">History</button></div></div></div>`; }
function transactionsHtml() { if (!state.transactions.length) return `<p class="muted">No transactions yet.</p>`; return `<div class="table-wrap"><table><thead><tr><th>Type</th><th>Amount</th><th>Account</th><th>Date</th></tr></thead><tbody>${state.transactions.slice(0,8).map(t => `<tr><td>${esc(t.transactionType)}</td><td>${money(t.amount)}</td><td>${t.sourceAccountId || t.destinationAccountId || '-'}</td><td>${date(t.createdAt)}</td></tr>`).join('')}</tbody></table></div>`; }

function modal(title, body) { state.modal = `<div class="modal-backdrop" onclick="if(event.target===this)closeModal()"><div class="modal"><div class="modal-head"><h2>${title}</h2><button class="btn ghost" onclick="closeModal()">×</button></div>${body}</div></div>`; renderDashboard(); }
function closeModal(){ state.modal=null; renderDashboard(); }
function openAccountModal(){ modal('Create account', `<div class="field"><label>Account type</label><select id="accountType"><option>SAVINGS</option><option>CURRENT</option></select></div><button class="btn full" onclick="createAccount()">Create account</button>`); }
async function createAccount(){ try { await api(`/accounts?userId=${state.userId}`, {method:'POST', body:JSON.stringify({accountType:document.getElementById('accountType').value})}); closeModal(); await dashboard(); } catch(e){ alert(e.message); } }
function openTransferModal(){
  if(state.accounts.length < 2){ alert('Create at least two accounts before transferring.'); return; }
  modal('Transfer money', `<div class="field"><label>From</label><select id="sourceId">${state.accounts.map(a=>`<option value="${a.id}">${esc(a.accountNumber)} — ${money(a.balance)}</option>`).join('')}</select></div><div class="field"><label>To</label><select id="destinationId">${state.accounts.map(a=>`<option value="${a.id}">${esc(a.accountNumber)}</option>`).join('')}</select></div><div class="field"><label>Amount (₹)</label><input id="transferAmount" type="number" min="0.01" step="0.01"></div><div class="field"><label>Description</label><input id="transferDescription" placeholder="Optional"></div><button class="btn full" onclick="transferMoney()">Confirm transfer</button>`);
}
async function transferMoney(){ try { await api('/transactions/transfer',{method:'POST',body:JSON.stringify({sourceAccountId:Number(document.getElementById('sourceId').value),destinationAccountId:Number(document.getElementById('destinationId').value),amount:Number(document.getElementById('transferAmount').value),description:document.getElementById('transferDescription').value})}); closeModal(); await dashboard(); } catch(e){ alert(e.message); } }
function openMoneyModal(type,id){ modal(type === 'deposit' ? 'Deposit money' : 'Withdraw money', `<div class="field"><label>Amount (₹)</label><input id="amount" type="number" min="0.01" step="0.01" placeholder="1000"></div><div class="field"><label>Description</label><input id="description" placeholder="Optional"></div><button class="btn full" onclick="moneyAction('${type}',${id})">Confirm ${type}</button>`); }
async function moneyAction(type,id){ try { await api(`/transactions/${type}/${id}`, {method:'POST',body:JSON.stringify({amount:Number(document.getElementById('amount').value),description:document.getElementById('description').value})}); closeModal(); await dashboard(); } catch(e){ alert(e.message); } }
function showAccountTransactions(id){ const rows=state.transactions.filter(t=>t.sourceAccountId===id||t.destinationAccountId===id); modal('Transaction history', rows.length ? `<div class="table-wrap"><table><thead><tr><th>Type</th><th>Amount</th><th>Date</th></tr></thead><tbody>${rows.map(t=>`<tr><td>${esc(t.transactionType)}</td><td>${money(t.amount)}</td><td>${date(t.createdAt)}</td></tr>`).join('')}</tbody></table></div>` : `<p class="muted">No transactions for this account.</p>`); }

if (state.token && state.userId) dashboard(); else authView('login');
