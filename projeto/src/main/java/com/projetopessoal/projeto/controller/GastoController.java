package com.projetopessoal.projeto.controller;

import com.projetopessoal.projeto.model.Gasto;
import com.projetopessoal.projeto.model.User;
import com.projetopessoal.projeto.repository.GastoRepository;
import com.projetopessoal.projeto.repository.UserRepository;
import com.projetopessoal.projeto.repository.ContaRepository;
import com.projetopessoal.projeto.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import com.projetopessoal.projeto.service.GeminiService;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    @Autowired
    private GastoRepository gastoRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GeminiService geminiService;

    // Construtor padrão para o Spring
    public GastoController() {}

    private User getAuthenticatedUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Sessão inválida.");
        }
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    }

    @PostMapping
    public ResponseEntity<?> criarGasto(@RequestBody Map<String, Object> payload, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            
            String descricao = (String) payload.get("descricao");
            Object valorObj = payload.get("valor");
            String status = payload.getOrDefault("status", "Pago").toString();

            if (descricao == null || valorObj == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Campos obrigatórios ausentes: descricao ou valor"));
            }

            Gasto novoGasto = new Gasto();
            novoGasto.setDescricao(descricao);
            novoGasto.setValor(Double.parseDouble(valorObj.toString()));
            novoGasto.setStatus(status);
            novoGasto.setTipo(payload.getOrDefault("tipo", "DESPESA").toString());
            
            if ("Parcelado".equalsIgnoreCase(status)) {
                if (payload.containsKey("numeroParcelas")) {
                    novoGasto.setNumeroParcelas(Integer.parseInt(payload.get("numeroParcelas").toString()));
                }
                if (payload.containsKey("valorParcela")) {
                    novoGasto.setValorParcela(Double.parseDouble(payload.get("valorParcela").toString()));
                }
            }

            if (payload.containsKey("contaId") && payload.get("contaId") != null) {
                Long contaId = Long.parseLong(payload.get("contaId").toString());
                Conta conta = contaRepository.findById(contaId).orElse(null);
                
                // Correção de Vulnerabilidade IDOR: Verifica se a conta pertence ao usuário
                if (conta != null && !conta.getUsuario().getId().equals(user.getId())) {
                    return ResponseEntity.status(403).body(Map.of("error", "Acesso negado a esta conta."));
                }
                
                novoGasto.setConta(conta);
            }

            novoGasto.setUsuario(user);
            
            Gasto salvo = gastoRepository.save(novoGasto);
            return ResponseEntity.ok(salvo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Falha interna ao salvar gasto"
            ));
        }
    }

    @PostMapping("/importar-caixa")
    public ResponseEntity<?> importarExtratoCaixa(
            @RequestParam("file") MultipartFile file, 
            @RequestParam("contaId") Long contaId,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = getAuthenticatedUser(userDetails);
            Conta conta = contaRepository.findById(contaId)
                    .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

            // Verifica se a conta pertence ao usuário autenticado (previne IDOR)
            if (!conta.getUsuario().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado a esta conta."));
            }

            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Arquivo vazio."));
            }

            // Validação de tipo de arquivo (apenas PDF)
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                return ResponseEntity.badRequest().body(Map.of("error", "Apenas arquivos PDF são aceitos."));
            }

            // Validação de tamanho (máximo 10MB)
            long maxSize = 10 * 1024 * 1024; // 10MB
            if (file.getSize() > maxSize) {
                return ResponseEntity.badRequest().body(Map.of("error", "O arquivo excede o tamanho máximo de 10MB."));
            }

            // Envia o PDF para o GeminiService
            List<Map<String, Object>> gastosExtraidos = geminiService.processarExtratoCaixa(file.getBytes());
            int salvos = 0;

            for (Map<String, Object> dado : gastosExtraidos) {
                Gasto novoGasto = new Gasto();
                novoGasto.setUsuario(user);
                novoGasto.setConta(conta);
                novoGasto.setDescricao((String) dado.get("descricao"));
                
                if (dado.containsKey("tipo")) {
                    novoGasto.setTipo((String) dado.get("tipo"));
                }
                if (dado.containsKey("nrDoc")) {
                    novoGasto.setNrDoc((String) dado.get("nrDoc"));
                }
                if (dado.containsKey("favorecido")) {
                    novoGasto.setFavorecido((String) dado.get("favorecido"));
                }
                if (dado.containsKey("cpfCnpj")) {
                    novoGasto.setCpfCnpj((String) dado.get("cpfCnpj"));
                }
                if (dado.containsKey("categoria")) {
                    novoGasto.setCategoria((String) dado.get("categoria"));
                } else {
                    novoGasto.setCategoria("Outros");
                }
                if (dado.containsKey("saldo") && dado.get("saldo") != null) {
                    try {
                        novoGasto.setSaldo(Double.parseDouble(dado.get("saldo").toString()));
                    } catch (Exception ignored) {}
                }
                
                // Converte o valor de forma segura
                Object valorObj = dado.get("valor");
                if (valorObj instanceof Number) {
                    novoGasto.setValor(((Number) valorObj).doubleValue());
                } else if (valorObj instanceof String) {
                    novoGasto.setValor(Double.parseDouble(valorObj.toString().replace(",", ".")));
                }

                // Trata a data (espera ISO-8601)
                if (dado.containsKey("dataGasto") && dado.get("dataGasto") != null) {
                    try {
                        novoGasto.setDataGasto(LocalDateTime.parse(dado.get("dataGasto").toString()));
                    } catch (Exception e) {
                        novoGasto.setDataGasto(LocalDateTime.now());
                    }
                } else {
                    novoGasto.setDataGasto(LocalDateTime.now());
                }

                novoGasto.setStatus("Pago"); // Extrato de conta sempre indica que já foi pago
                gastoRepository.save(novoGasto);
                salvos++;
            }

            return ResponseEntity.ok(Map.of("message", "Extrato processado com sucesso!", "lidos", salvos));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Falha ao processar o extrato."));
        }
    }


    @GetMapping
    public ResponseEntity<?> getGastos(@RequestParam(required = false) Long contaId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        if (contaId != null) {
            Conta conta = contaRepository.findById(contaId).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
            // Verifica se a conta pertence ao usuário autenticado (previne IDOR)
            if (!conta.getUsuario().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado a esta conta."));
            }
            return ResponseEntity.ok(gastoRepository.findByUsuarioAndConta(user, conta));
        }
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
                if (payload.containsKey("status")) {
                    gasto.setStatus((String) payload.get("status"));
                }
                if (payload.containsKey("tipo")) {
                    gasto.setTipo((String) payload.get("tipo"));
                }
                if (payload.containsKey("numeroParcelas")) {
                    gasto.setNumeroParcelas(Integer.parseInt(payload.get("numeroParcelas").toString()));
                }
                if (payload.containsKey("valorParcela")) {
                    gasto.setValorParcela(Double.parseDouble(payload.get("valorParcela").toString()));
                }
                return ResponseEntity.ok(gastoRepository.save(gasto));
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Falha ao atualizar transação."));
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
            return ResponseEntity.internalServerError().body(Map.of("error", "Falha ao remover transação."));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestParam(required = false) Long contaId, @AuthenticationPrincipal UserDetails userDetails) {
        User user = getAuthenticatedUser(userDetails);
        List<Gasto> gastos;
        if (contaId != null) {
            Conta conta = contaRepository.findById(contaId).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
            // Verifica se a conta pertence ao usuário autenticado (previne IDOR)
            if (!conta.getUsuario().getId().equals(user.getId())) {
                return ResponseEntity.status(403).body(Map.of("error", "Acesso negado a esta conta."));
            }
            gastos = gastoRepository.findByUsuarioAndConta(user, conta);
        } else {
            gastos = gastoRepository.findByUsuario(user);
        }
        
        long totalGastos = gastos.size();
        double valorTotal = gastos.stream().mapToDouble(Gasto::getValor).sum();
        
        // Calcula o gasto fixo mensal (soma das parcelas de gastos parcelados)
        double gastoFixoMensal = gastos.stream()
            .filter(g -> "Parcelado".equalsIgnoreCase(g.getStatus()))
            .mapToDouble(g -> g.getValorParcela() != null ? g.getValorParcela() : 0.0)
            .sum();
        
        return ResponseEntity.ok(Map.of(
            "totalGastos", totalGastos,
            "valorTotal", valorTotal,
            "gastoFixoMensal", gastoFixoMensal
        ));
    }
}
