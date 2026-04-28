import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private http = inject(HttpClient);
  private readonly API_URL = 'http://localhost:8081/api/pedidos'; // Altere para sua URL

  getOrders(): Observable<any[]> {
    return this.http.get<any[]>(this.API_URL);
  }

  createOrder(order: { descricao: string, valor: number }): Observable<any> {
    return this.http.post<any>(this.API_URL, order);
  }

  updateOrder(id: number, order: { descricao: string, valor: number }): Observable<any> {
    return this.http.put<any>(`${this.API_URL}/${id}`, order);
  }

  deleteOrder(id: number): Observable<any> {
    return this.http.delete<any>(`${this.API_URL}/${id}`);
  }

  getKpis(): Observable<any> {
    return this.http.get<any>(`${this.API_URL}/stats`);
  }
}
