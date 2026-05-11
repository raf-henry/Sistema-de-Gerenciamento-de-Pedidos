import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('auth_token');

  if (token && isTokenValid(token)) {
    return true;
  } else {
    // Token ausente ou expirado — limpa o storage e redireciona
    localStorage.removeItem('auth_token');
    localStorage.removeItem('username');
    router.navigate(['/login']);
    return false;
  }
};

/**
 * Decodifica o payload do JWT e verifica se o campo 'exp' ainda é válido.
 * Retorna false se o token estiver malformado ou expirado.
 */
function isTokenValid(token: string): boolean {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return false;

    const payload = JSON.parse(atob(parts[1]));
    if (!payload.exp) return false;

    // payload.exp está em segundos desde epoch
    const expirationDate = new Date(payload.exp * 1000);
    return expirationDate > new Date();
  } catch {
    return false;
  }
}
