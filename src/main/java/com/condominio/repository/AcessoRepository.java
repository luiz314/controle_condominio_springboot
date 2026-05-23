package com.condominio.repository;

import com.condominio.entity.Acesso;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AcessoRepository extends JpaRepository<Acesso, Long> {

    Optional<Acesso> findTopByPessoaIdAndSaidaIsNullOrderByEntradaDesc(Long pessoaId);

    List<Acesso> findByPessoaIdOrderByEntradaDesc(Long pessoaId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Acesso a WHERE a.pessoa.id = :pessoaId")
    void deleteByPessoaId(@Param("pessoaId") Long pessoaId);
}
