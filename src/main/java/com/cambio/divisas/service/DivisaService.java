package com.cambio.divisas.service;

import com.cambio.divisas.domain.request.DivisaDto;

import java.util.List;

public interface DivisaService {

    DivisaDto getDivisaById(Integer id);

    List<DivisaDto> getDivisas();

    String deleteDivisaById(Integer id);

    public void actualizarDivisa(Integer id, Double nuevoPrecio);

}
