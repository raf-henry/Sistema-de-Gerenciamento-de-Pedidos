import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private router = inject(Router);
  private orderService = inject(OrderService);

  // Usando Signals para garantir a atualização da tela
  orders = signal<any[]>([]);
  stats = signal({
    todayOrders: 0,
    valorTotal: 0
  });

  showModal = false;
  novoPedido = {
    descricao: '',
    valor: 0
  };

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    console.log('Buscando dados do servidor...');
    
    // Busca pedidos
    this.orderService.getOrders().subscribe({
      next: (data) => {
        console.log('Dados recebidos:', data);
        this.orders.set(data); // Atualiza o signal
      },
      error: (err) => console.error('Erro ao buscar pedidos', err)
    });

    // Busca estatísticas
    this.orderService.getKpis().subscribe({
      next: (data) => {
        this.stats.set({
          todayOrders: data.totalPedidos,
          valorTotal: data.valorTotal
        });
      },
      error: (err) => console.error('Erro ao buscar estatísticas', err)
    });
  }

  salvarPedido() {
    if (this.novoPedido.descricao && this.novoPedido.valor > 0) {
      this.orderService.createOrder(this.novoPedido).subscribe({
        next: () => {
          this.showModal = false;
          this.novoPedido = { descricao: '', valor: 0 };
          this.loadDashboardData();
        },
        error: (err) => alert('Erro ao salvar: ' + err.message)
      });
    }
  }

  logout() {
    this.router.navigate(['/login']);
  }
}
