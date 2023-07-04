package com.teste.marketplace.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.teste.marketplace.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query("SELECT c.cliente FROM Compra c WHERE MONTH(c.dataCompra) = :mes " +
           "GROUP BY c.cliente " +
           "ORDER BY COUNT(c.cliente) DESC")
    List<String> findClienteMaisComprasPorMes(@Param("mes") int mes);
}
