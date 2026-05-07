package com.projetopessoal.projeto.repository;

import com.projetopessoal.projeto.model.Conta;
import com.projetopessoal.projeto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    List<Conta> findByUsuario(User usuario);
}
