package com.projetopessoal.projeto.repository;

import com.projetopessoal.projeto.model.Gasto;
import com.projetopessoal.projeto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByUsuario(User usuario);
    List<Gasto> findByUsuarioAndConta(User usuario, com.projetopessoal.projeto.model.Conta conta);
}
