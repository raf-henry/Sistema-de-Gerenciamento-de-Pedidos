import { Routes } from '@angular/router';
import { Home } from './components/home/home';
import { Contas } from './components/contas/contas';
import { Login } from './components/login/login';
import { Register } from './components/register/register';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'contas', component: Contas },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
];
