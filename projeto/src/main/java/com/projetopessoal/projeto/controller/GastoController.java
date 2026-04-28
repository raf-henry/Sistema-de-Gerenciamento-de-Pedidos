package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.model.Gasto;
import com.projetopessoal.projeto.model.User;
import com.projetopessoal.projeto.repository.GastoRepository;
import com.projetopessoal.projeto.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gastos")
@CrossOrigin(origins = "http://localhost:4200")
public class GastoController {

    private final GastoRepository gastoRepository;
    private final UserRepository userRepository;

    public GastoController(GastoRepository gastoRepository, UserRepository userRepository) {
        this.gastoRepository = gastoRepository;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @PostMapping
    public ResponseEntity<?> criarGasto(@RequestBody Map<String, Object> payload, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            Gasto novoGasto = new Gasto();
            novoGasto.setDescricao((String) payload.get("descricao"));
            novoGasto.setValor(Double.parseDouble(payload.get("valor").toString()));
            novoGasto.setUsuario(user);
            
            Gasto salvo = gastoRepository.save(novoGasto);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Gasto>> getGastos(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        return ResponseEntity.ok(gastoRepository.findByUsuario(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarGasto(@PathVariable Long id, @RequestBody Map<String, Object> payload, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            return gastoRepository.findById(id).map(gasto -> {
                if (!gasto.getUsuario().getId().equals(user.getId())) {
                    return ResponseEntity.status(403).body(Map.of("error", "Não autorizado"));
                }
                if (payload.containsKey("descricao")) {
                    gasto.setDescricao((String) payload.get("descricao"));
                }
                if (payload.containsKey("valor")) {
                    gasto.setValor(Double.parseDouble(payload.get("valor").toString()));
                }
                return ResponseEntity.ok(gastoRepository.save(gasto));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deletarGasto(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            return gastoRepository.findById(id).map(gasto -> {
                if (!gasto.getUsuario().getId().equals(user.getId())) {
                    return ResponseEntity.status(403).body(Map.of("error", "Não autorizado"));
                }
                gastoRepository.deleteById(id);
                return ResponseEntity.ok(Map.of("message", "Gasto removido com sucesso"));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        List<Gasto> gastos = gastoRepository.findByUsuario(user);
        long totalGastos = gastos.size();
        double valorTotal = gastos.stream().mapToDouble(Gasto::getValor).sum();
        
        return ResponseEntity.ok(Map.of(
            "totalGastos", totalGastos,
            "valorTotal", valorTotal
        ));
    }
}
