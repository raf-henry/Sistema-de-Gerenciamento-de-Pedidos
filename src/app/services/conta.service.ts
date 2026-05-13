import { Injectable, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

export interface Conta {
  id?: number;
  nome: string;
  banco: string;
  tipo: string;
  cor: string;
  icone: string;
}

@Injectable({
  providedIn: 'root'
})
export class ContaService {
  private apiUrl = `${environment.apiUrl}/api/contas`;
  
  // Signal para manter as contas sincronizadas em toda a aplicação
  contas = signal<Conta[]>([]);
  selectedContaId = signal<number | null>(null);

  constructor(private http: HttpClient, private auth: AuthService) {}

  private getHeaders() {
    const token = localStorage.getItem('auth_token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getContas(): Observable<Conta[]> {
    return this.http.get<Conta[]>(this.apiUrl, { headers: this.getHeaders() }).pipe(
      tap(contas => this.contas.set(contas))
    );
  }

  criarConta(conta: Conta): Observable<Conta> {
    return this.http.post<Conta>(this.apiUrl, conta, { headers: this.getHeaders() }).pipe(
      tap(() => this.getContas().subscribe()) // Recarrega a lista após criar
    );
  }

  deletarConta(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`, { headers: this.getHeaders() }).pipe(
      tap(() => this.getContas().subscribe()) // Recarrega a lista após deletar
    );
  }
}
