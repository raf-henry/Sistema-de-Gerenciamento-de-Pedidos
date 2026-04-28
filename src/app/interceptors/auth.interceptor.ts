import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Ignora requisições de login e registro para não enviar token inválido
  if (req.url.includes('/api/auth/')) {
    return next(req);
  }

  const token = localStorage.getItem('auth_token');

  if (token && token !== 'undefined' && token !== 'null') {
    const cloned = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(cloned);
  }

  return next(req);
};
