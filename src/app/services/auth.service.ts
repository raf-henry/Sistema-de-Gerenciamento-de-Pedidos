import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/auth';

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, credentials).pipe(
      tap((response: any) => {
        // Salva as credenciais em Base64 para usar nas próximas requisições
        const authData = btoa(`${credentials.email}:${credentials.password}`);
        localStorage.setItem('auth_token', authData);
        localStorage.setItem('username', credentials.email);
      })
    );
  }

  getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders({
      'Authorization': `Basic ${token}`
    });
  }

  logout() {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('username');
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, userData);
  }
}
