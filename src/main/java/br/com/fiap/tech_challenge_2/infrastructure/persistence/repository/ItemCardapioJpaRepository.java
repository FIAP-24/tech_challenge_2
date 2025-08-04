package br.com.fiap.tech_challenge_2.infrastructure.persistence.repository;

import br.com.fiap.tech_challenge_2.infrastructure.persistence.entity.ItemCardapioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCardapioJpaRepository extends JpaRepository<ItemCardapioEntity, Long> {

    @Modifying
    @Query("DELETE FROM ItemCardapioEntity i WHERE i.id = :id")
    void deleteItemById(@Param("id") Long id);



    List<ItemCardapioEntity> findByRestauranteId(Long restauranteId);
    
    List<ItemCardapioEntity> findByRestauranteIdAndDisponivelApenasNoLocalFalse(Long restauranteId);
}