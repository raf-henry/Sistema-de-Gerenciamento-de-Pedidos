package com.projetopessoal.projeto.repository;

import com.projetopessoal.projeto.model.Pedido;
import com.projetopessoal.projeto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(User usuario);
}
