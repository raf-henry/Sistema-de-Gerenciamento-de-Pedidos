import { Component, inject, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { ExpenseService } from '../../services/expense.service';
import { AuthService } from '../../services/auth.service';
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
  private expenseService = inject(ExpenseService);
  private authService = inject(AuthService);

  expenses = signal<any[]>([]);
  stats = signal({
    totalGastos: 0,
    valorTotal: 0
  });

  userName = localStorage.getItem('username') || 'Usuário';

  showModal = false;
  isEditing = false;
  selectedExpenseId: number | null = null;
  
  novoGasto = {
    descricao: '',
    valor: 0
  };

  ngOnInit() {
    this.loadDashboardData();
  }

  loadDashboardData() {
    this.expenseService.getExpenses().subscribe({
      next: (data) => this.expenses.set(data),
      error: (err) => console.error('Erro ao buscar gastos', err)
    });

    this.expenseService.getKpis().subscribe({
      next: (data) => {
        this.stats.set({
          totalGastos: data.totalGastos,
          valorTotal: data.valorTotal
        });
      },
      error: (err) => console.error('Erro ao buscar estatísticas', err)
    });
  }

  abrirModalParaNovo() {
    this.isEditing = false;
    this.selectedExpenseId = null;
    this.novoGasto = { descricao: '', valor: 0 };
    this.showModal = true;
  }

  abrirModalParaEditar(gasto: any) {
    this.isEditing = true;
    this.selectedExpenseId = gasto.id;
    this.novoGasto = { 
      descricao: gasto.descricao, 
      valor: gasto.valor 
    };
    this.showModal = true;
  }

  salvarGasto() {
    if (this.novoGasto.descricao && this.novoGasto.valor > 0) {
      // Capitaliza a primeira letra
      const descricaoFormatada = this.novoGasto.descricao.charAt(0).toUpperCase() + this.novoGasto.descricao.slice(1);
      const gastoParaSalvar = { ...this.novoGasto, descricao: descricaoFormatada };

      if (this.isEditing && this.selectedExpenseId) {
        this.expenseService.updateExpense(this.selectedExpenseId, gastoParaSalvar).subscribe({
          next: () => this.finalizarOperacao(),
          error: (err) => alert('Erro ao atualizar: ' + err.message)
        });
      } else {
        this.expenseService.createExpense(gastoParaSalvar).subscribe({
          next: () => this.finalizarOperacao(),
          error: (err) => alert('Erro ao salvar: ' + err.message)
        });
      }
    }
  }

  deletarGasto(id: number) {
    if (confirm('Tem certeza que deseja excluir este gasto?')) {
      this.expenseService.deleteExpense(id).subscribe({
        next: () => this.loadDashboardData(),
        error: (err) => alert('Erro ao excluir: ' + err.message)
      });
    }
  }

  private finalizarOperacao() {
    this.showModal = false;
    this.novoGasto = { descricao: '', valor: 0 };
    this.loadDashboardData();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
