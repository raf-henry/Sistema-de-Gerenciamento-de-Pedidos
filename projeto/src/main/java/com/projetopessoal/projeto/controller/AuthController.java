package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.config.JwtUtils;
import com.projetopessoal.projeto.model.User;
import com.projetopessoal.projeto.repository.UserRepository;
import com.projetopessoal.projeto.service.VerificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final VerificationService verificationService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils, VerificationService verificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
        this.verificationService = verificationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        Optional<User> userOpt = userRepository.findByUsername(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtils.generateToken(user.getUsername());
                return ResponseEntity.ok(Map.of(
                        "token", token,
                        "username", user.getUsername(),
                        "message", "Login successful"
                ));
            }
        }
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "E-mail inválido."));
        }
        if (userRepository.findByUsername(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário já existe."));
        }
        String code = verificationService.generateAndSendCode(email);
        return ResponseEntity.ok(Map.of(
            "message", "Código gerado com sucesso!",
            "demoCode", code
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> userData) {
        String email = userData.get("email");
        String password = userData.get("password");
        String code = userData.get("code");

        if (email == null || password == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Preencha todos os campos, incluindo o código de verificação."));
        }

        if (password.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("error", "A senha deve ter pelo menos 8 caracteres."));
        }

        if (userRepository.findByUsername(email).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Usuário já existe"));
        }

        boolean isValid = verificationService.verifyCode(email, code);
        if (!isValid) {
            return ResponseEntity.status(400).body(Map.of("error", "Código de verificação inválido ou expirado."));
        }

        User newUser = new User();
        newUser.setUsername(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole("USER");

        userRepository.save(newUser);
        
        // Também vamos gerar e retornar um token para autenticar o usuário logo após o registro
        String token = jwtUtils.generateToken(newUser.getUsername());
        
        return ResponseEntity.ok(Map.of(
            "message", "Usuário registrado e autenticado com sucesso!",
            "token", token,
            "username", newUser.getUsername()
        ));
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Map<String, String> body) {
        String currentPassword = body.get("currentPassword");
        String newPassword = body.get("newPassword");

        if (currentPassword == null || newPassword == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Preencha todos os campos."));
        }

        if (newPassword.length() < 8) {
            return ResponseEntity.badRequest().body(Map.of("error", "A nova senha deve ter pelo menos 8 caracteres."));
        }

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtils.extractUsername(token);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(400).body(Map.of("error", "Senha atual incorreta"));
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "Senha alterada com sucesso!"));
    }

    @PutMapping("/change-email")
    public ResponseEntity<?> changeEmail(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Map<String, String> body) {
        String currentPassword = body.get("currentPassword");
        String newEmail = body.get("newEmail");

        String token = authHeader.replace("Bearer ", "");
        String username = jwtUtils.extractUsername(token);

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body(Map.of("error", "Usuário não encontrado"));
        }

        User user = userOpt.get();
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            return ResponseEntity.status(400).body(Map.of("error", "Senha incorreta"));
        }

        if (userRepository.findByUsername(newEmail).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Este e-mail já está em uso"));
        }

        user.setUsername(newEmail);
        userRepository.save(user);

        String newToken = jwtUtils.generateToken(newEmail);
        return ResponseEntity.ok(Map.of(
                "message", "E-mail alterado com sucesso!",
                "token", newToken,
                "username", newEmail
        ));
    }

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmail(@RequestParam String email) {
        boolean exists = userRepository.findByUsername(email).isPresent();
        return ResponseEntity.ok(Map.of("exists", exists));
    }
}
