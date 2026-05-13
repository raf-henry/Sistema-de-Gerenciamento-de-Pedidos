import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);

  loginData = {
    email: '',
    password: ''
  };

  onSubmit() {
    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Erro no login');
        alert('Falha ao autenticar. Verifique suas credenciais.');
      }
    });
  }
}
