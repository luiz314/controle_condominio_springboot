package com.condominio.repository;

import com.condominio.entity.Acesso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    Optional<Acesso> findTopByPessoaIdAndSaidaIsNullOrderByEntradaDesc(Long pessoaId);

    List<Acesso> findByPessoaIdOrderByEntradaDesc(Long pessoaId);
}
