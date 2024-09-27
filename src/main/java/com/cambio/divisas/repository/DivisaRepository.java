package com.cambio.divisas.repository;

import com.cambio.divisas.domain.Divisa;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DivisaRepository extends JpaRepository<Divisa, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Divisa d SET d.precioDeCambio = :nuevoPrecio, d.montoFinal = ROUND(d.monto * :nuevoPrecio, 2) " +
            "WHERE d.id = :id")
    int actualizarPrecioCambioYMontoFinal(@Param("id") Integer id, @Param("nuevoPrecio") Double nuevoPrecio);

}
