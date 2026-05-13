import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Contas } from './components/contas/contas';
import { Transacoes } from './components/transacoes/transacoes';
import { Login } from './components/login/login';
import { Register } from './components/register/register';
import { Configuracoes } from './components/configuracoes/configuracoes';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'home', component: Home, canActivate: [authGuard] },
  { path: 'contas', component: Contas, canActivate: [authGuard] },
  { path: 'transacoes', component: Transacoes, canActivate: [authGuard] },
  { path: 'configuracoes', component: Configuracoes, canActivate: [authGuard] },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: '**', redirectTo: 'login' },
];
