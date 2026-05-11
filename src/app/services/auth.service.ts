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
        if (response.token) {
          localStorage.setItem('auth_token', response.token);
          localStorage.setItem('username', credentials.email);
        }
      })
    );
  }

  logout() {
    localStorage.removeItem('auth_token');
    localStorage.removeItem('username');
  }

  register(userData: {email: string, password: string, code: string}): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, userData).pipe(
      tap((response: any) => {
        if (response.token) {
          localStorage.setItem('auth_token', response.token);
          localStorage.setItem('username', response.username || userData.email);
        }
      })
    );
  }

  sendVerificationCode(email: string): Observable<any> {
    return this.http.post(`${this.API_URL}/send-code`, { email });
  }
}
