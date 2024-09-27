package com.cambio.divisas.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "table_divisa")
public class Divisa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String monedaOrigen;
    private String monedaDestino;
    private Double monto;
    private Double precioDeCambio;
    private Double montoFinal;

}
