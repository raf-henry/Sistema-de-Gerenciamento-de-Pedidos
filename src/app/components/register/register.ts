import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthService);
  private router = inject(Router);

  registerData = {
    email: '',
    password: '',
    confirmPassword: ''
  };

  onSubmit() {
    if (this.registerData.password !== this.registerData.confirmPassword) {
      alert('As senhas não coincidem!');
      return;
    }

    const { email, password } = this.registerData;
    this.authService.register({ email, password }).subscribe({
      next: (response) => {
        alert('Cadastro realizado com sucesso! Agora faça login.');
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Erro no cadastro', err);
        alert('Falha ao cadastrar. Tente novamente.');
      }
    });
  }
}
