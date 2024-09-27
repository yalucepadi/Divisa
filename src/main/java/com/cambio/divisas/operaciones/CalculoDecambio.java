package com.cambio.divisas.operaciones;

import com.cambio.divisas.domain.Divisa;

public class CalculoDecambio {
    public String cambio(Divisa divisa) {
        if (divisa.getMonedaOrigen().isEmpty() || divisa.getMonedaDestino().isEmpty() || divisa.getMonto() == 0.0 ||
                divisa.getPrecioDeCambio() == 0.0) {
            return "Datos incompletos no se puede realizar operacion de cambio";

        } else {
            Double montoFinal = divisa.getMontoFinal();
            montoFinal = divisa.getMonto() * divisa.getPrecioDeCambio();
            return
                    String.valueOf(String.format("%.2f", montoFinal));

        }


    }

}
