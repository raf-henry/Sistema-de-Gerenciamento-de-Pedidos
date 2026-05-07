import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/gastos';

  getExpenses(): Observable<any[]> {
    return this.http.get<any[]>(this.API_URL);
  }

  createExpense(expense: { descricao: string, valor: number }): Observable<any> {
    return this.http.post<any>(this.API_URL, expense);
  }

  updateExpense(id: number, expense: { descricao: string, valor: number }): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, expense);
  }

  deleteExpense(id: number): Observable<any> {
    return this.http.delete<any>(`${this.API_URL}/${id}`);
  }

  getKpis(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/stats`);
  }

  uploadExtrato(file: File, contaId: string): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('contaId', contaId);
    return this.http.post<any>(`${this.API_URL}/importar-caixa`, formData);
  }
}
