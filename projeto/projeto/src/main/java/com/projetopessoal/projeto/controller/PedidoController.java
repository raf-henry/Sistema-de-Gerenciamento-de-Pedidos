package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.model.Pedido;
import com.projetopessoal.projeto.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "http://localhost:4200") // Permite o Angular acessar o Java
public class PedidoController {

    private final PedidoRepository pedidoRepository;
    private final com.projetopessoal.projeto.repository.UserRepository userRepository;

    public PedidoController(PedidoRepository pedidoRepository, com.projetopessoal.projeto.repository.UserRepository userRepository) {
        this.pedidoRepository = pedidoRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarPedido(@RequestBody Map<String, Object> payload) {
        try {
            Pedido novoPedido = new Pedido();
            novoPedido.setDescricao((String) payload.get("descricao"));
            novoPedido.setValor(Double.parseDouble(payload.get("valor").toString()));
            
            // Verifica se existem usuários no banco
            var usuarios = userRepository.findAll();
            if (usuarios.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Nenhum usuário encontrado no banco para associar ao pedido."));
            }
            
            novoPedido.setUsuario(usuarios.get(0));
            Pedido salvo = pedidoRepository.save(novoPedido);
            System.out.println("Pedido salvo com sucesso! ID: " + salvo.getId());
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> getPedidos() {
        return ResponseEntity.ok(pedidoRepository.findAll());
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
        long totalPedidos = pedidoRepository.count();
        double valorTotal = pedidoRepository.findAll().stream()
                .mapToDouble(Pedido::getValor)
                .sum();
        
        return ResponseEntity.ok(Map.of(
            "totalPedidos", totalPedidos,
            "valorTotal", valorTotal
        ));
    }
}
