import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { tap, catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Ignora requisições de login e registro para não enviar token inválido
  if (req.url.includes('/api/auth/')) {
    return next(req);
  }

  const token = localStorage.getItem('auth_token');

  let request = req;
  if (token && token !== 'undefined' && token !== 'null') {
    request = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(request).pipe(
    catchError((error) => {
      // Se o backend retornar 401 (token inválido/expirado), limpa o storage e redireciona
      if (error.status === 401) {
        localStorage.removeItem('auth_token');
        localStorage.removeItem('username');
        // Redireciona para login apenas se não estiver já na página de login
        if (!window.location.pathname.includes('/login')) {
          window.location.href = '/login';
        }
      }
      return throwError(() => error);
    })
  );
};
