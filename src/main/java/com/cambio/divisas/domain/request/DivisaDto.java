package com.cambio.divisas.domain.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DivisaDto {
    private Integer id;
    private String monedaOrigen;
    private String monedaDestino;
    private Double monto;
    private Double precioDeCambio;
    private Double montoFinal;
}
