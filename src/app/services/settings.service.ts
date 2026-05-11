import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SettingsService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/auth';

  changePassword(currentPassword: string, newPassword: string): Observable<any> {
    return this.http.put(`${this.API_URL}/change-password`, {
      currentPassword,
      newPassword
    });
  }

  changeEmail(currentPassword: string, newEmail: string): Observable<any> {
    return this.http.put(`${this.API_URL}/change-email`, {
      currentPassword,
      newEmail
    });
  }

  checkEmail(email: string): Observable<{ exists: boolean }> {
    return this.http.get<{ exists: boolean }>(`${this.API_URL}/check-email?email=${encodeURIComponent(email)}`);
  }
}
