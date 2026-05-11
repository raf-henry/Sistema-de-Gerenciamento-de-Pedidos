import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ExpenseService {
  private http = inject(HttpClient);
  private readonly API_URL = `${environment.apiUrl}/api/gastos`;

  getExpenses(contaId?: number | null): Observable<any[]> {
    const url = contaId ? `${this.API_URL}?contaId=${contaId}` : this.API_URL;
    return this.http.get<any[]>(url);
  }

  createExpense(expense: { descricao: string, valor: number, contaId?: number }): Observable<any> {
    return this.http.post<any>(this.API_URL, expense);
  }

  updateExpense(id: number, expense: { descricao: string, valor: number, contaId?: number }): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, expense);
  }

  deleteExpense(id: number): Observable<any> {
    return this.http.delete<any>(`${this.API_URL}/${id}`);
  }

  getKpis(contaId?: number | null): Observable<any> {
    const url = contaId ? `${this.API_URL}/stats?contaId=${contaId}` : `${this.API_URL}/stats`;
    return this.http.get<any>(url);
  }

  uploadExtrato(file: File, contaId: string): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('contaId', contaId);
    return this.http.post<any>(`${this.API_URL}/importar-caixa`, formData);
  }
}
