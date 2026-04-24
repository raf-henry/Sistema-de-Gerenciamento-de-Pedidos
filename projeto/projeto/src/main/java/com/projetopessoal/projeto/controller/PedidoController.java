package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.model.Pedido;
import com.projetopessoal.projeto.repository.PedidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;

    public PedidoController(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
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
