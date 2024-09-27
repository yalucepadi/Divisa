package com.cambio.divisas.service;

import com.cambio.divisas.domain.request.DivisaDto;

public interface DivisaCalculoService {


    String calculoCambioOperacion(DivisaDto divisaDto);
}
