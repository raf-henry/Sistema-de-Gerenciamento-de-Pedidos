import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {
  private http = inject(HttpClient);
  private authService = inject(AuthService);
  private readonly API_URL = 'http://localhost:8081/api/gastos';

  getExpenses(): Observable<any[]> {
    return this.http.get<any[]>(this.API_URL, { headers: this.authService.getAuthHeaders() });
  }

  createExpense(expense: { descricao: string, valor: number }): Observable<any> {
    return this.http.post<any>(this.API_URL, expense, { headers: this.authService.getAuthHeaders() });
  }

  updateExpense(id: number, expense: { descricao: string, valor: number }): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, expense, { headers: this.authService.getAuthHeaders() });
  }

  deleteExpense(id: number): Observable<any> {
    return this.http.delete<any>(`${this.API_URL}/${id}`, { headers: this.authService.getAuthHeaders() });
  }

  getKpis(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/stats`, { headers: this.authService.getAuthHeaders() });
  }
}
