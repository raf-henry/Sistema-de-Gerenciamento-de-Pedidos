import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  private router = inject(Router);
  private orderService = inject(OrderService);

  orders: any[] = [];
  stats = {
    todayOrders: 0,
    pendingPayment: 0,
    newClients: 0
  };

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.orderService.getOrders().subscribe({
      next: (data) => this.orders = data,
      error: (err) => console.error('Erro ao buscar pedidos', err)
    });

    this.orderService.getKpis().subscribe({
      next: (data) => this.stats = data,
      error: (err) => console.error('Erro ao buscar estatísticas', err)
    });
  }

  logout() {
    this.router.navigate(['/login']);
  }
}
