import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const username = localStorage.getItem('username');

  // Com Cookies HttpOnly, o Frontend não consegue ler o token.
  // Usamos o 'username' apenas como um indicador visual de login.
  // A segurança REAL é feita pelo Backend validando o Cookie em cada requisição.
  if (username) {
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
