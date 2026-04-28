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

  orders = signal<any[]>([]);
  stats = signal({
    todayOrders: 0,
    valorTotal: 0
  });

  showModal = false;
  isEditing = false;
  selectedOrderId: number | null = null;
  
  novoPedido = {
    descricao: '',
    valor: 0
  };

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.orderService.getOrders().subscribe({
      next: (data) => this.orders.set(data),
      error: (err) => console.error('Erro ao buscar pedidos', err)
    });

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

  abrirModalParaNovo() {
    this.isEditing = false;
    this.selectedOrderId = null;
    this.novoPedido = { descricao: '', valor: 0 };
    this.showModal = true;
  }

  abrirModalParaEditar(pedido: any) {
    this.isEditing = true;
    this.selectedOrderId = pedido.id;
    this.novoPedido = { 
      descricao: pedido.descricao, 
      valor: pedido.valor 
    };
    this.showModal = true;
  }

  salvarPedido() {
    if (this.novoPedido.descricao && this.novoPedido.valor > 0) {
      if (this.isEditing && this.selectedOrderId) {
        this.orderService.updateOrder(this.selectedOrderId, this.novoPedido).subscribe({
          next: () => this.finalizarOperacao(),
          error: (err) => alert('Erro ao atualizar: ' + err.message)
        });
      } else {
        this.orderService.createOrder(this.novoPedido).subscribe({
          next: () => this.finalizarOperacao(),
          error: (err) => alert('Erro ao salvar: ' + err.message)
        });
      }
    }
  }

  deletarPedido(id: number) {
    if (confirm('Tem certeza que deseja excluir este pedido?')) {
      this.orderService.deleteOrder(id).subscribe({
        next: () => this.loadDashboardData(),
        error: (err) => alert('Erro ao excluir: ' + err.message)
      });
    }
  }

  private finalizarOperacao() {
    this.showModal = false;
    this.novoPedido = { descricao: '', margin: 0, valor: 0 } as any; // reset
    this.novoPedido = { descricao: '', valor: 0 };
    this.loadDashboardData();
  }

  logout() {
    this.router.navigate(['/login']);
  }
}
