import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/auth`;

  /**
   * Envia as credenciais para o backend, captura o username e inicia a sessão via cookies.
   */
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, credentials).pipe(
      tap((response: any) => {
        if (response.username) {
          localStorage.setItem('username', response.username);
        }
      })
    );
  }

  /**
   * Solicita o encerramento da sessão no backend e limpa os dados locais do usuário.
   */
  logout() {
    this.http.post(`${this.API_URL}/logout`, {}).subscribe(() => {
      localStorage.removeItem('username');
      window.location.href = '/login';
    });
  }

  /**
   * Realiza o registro de um novo usuário após a validação do código de e-mail.
   */
  register(userData: {email: string, password: string, code: string}): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, userData).pipe(
      tap((response: any) => {
        if (response.username) {
          localStorage.setItem('username', response.username);
        }
      })
    );
  }

  sendVerificationCode(email: string): Observable<any> {
    return this.http.post(`${this.API_URL}/send-code`, { email });
  }
}
