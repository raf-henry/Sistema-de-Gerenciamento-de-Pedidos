package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.model.Conta;
import com.projetopessoal.projeto.model.User;
import com.projetopessoal.projeto.repository.ContaRepository;
import com.projetopessoal.projeto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contas")
@CrossOrigin(origins = "*")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<Conta> getContas(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return contaRepository.findByUsuario(user);
    }

    @PostMapping
    public ResponseEntity<?> criarConta(@RequestBody Conta conta, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        conta.setUsuario(user);
        
        // Atribui cores padrões baseadas no banco se não enviadas ou vazias
        if (conta.getCor() == null || conta.getCor().trim().isEmpty()) {
            if (conta.getBanco().equalsIgnoreCase("CAIXA")) conta.setCor("bg-blue-700");
            else if (conta.getBanco().equalsIgnoreCase("NUBANK")) conta.setCor("bg-purple-600");
            else if (conta.getBanco().equalsIgnoreCase("ITAÚ")) conta.setCor("bg-orange-500");
            else conta.setCor("bg-gray-600");
        }

        Conta salva = contaRepository.save(conta);
        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConta(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Conta conta = contaRepository.findById(id).orElseThrow();
        if (!conta.getUsuario().getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(403).build();
        }
        contaRepository.delete(conta);
        return ResponseEntity.ok().build();
    }
}
