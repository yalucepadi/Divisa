package com.cambio.divisas.service;

import com.cambio.divisas.domain.Divisa;
import com.cambio.divisas.domain.request.DivisaDto;
import com.cambio.divisas.operaciones.CalculoDecambio;
import com.cambio.divisas.repository.DivisaRepository;
import com.cambio.divisas.util.MapperDivisa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DivisaServiceImpl implements DivisaCalculoService, DivisaService {

    @Autowired
    DivisaRepository divisaRepository;

    @Autowired
    MapperDivisa divisaMapper;

    @Override
    public DivisaDto getDivisaById(Integer id) {
        Divisa divisa = divisaRepository.findById(id).orElseThrow();
        return divisaMapper.toDto(divisa);
    }

    @Override
    public List<DivisaDto> getDivisas() {
        List<Divisa> divisas = divisaRepository.findAll();
        return divisas.stream()
                .map(divisaMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public String deleteDivisaById(Integer id) {
        Optional<Divisa> idDivisa = divisaRepository.findById(id);

        if (idDivisa.isPresent()) {
            divisaRepository.deleteById(id);
            return "Divisa fue eliminada";

        } else {
            return "Divisa no existe";

        }

    }

    @Override
    public void actualizarDivisa(Integer id, Double nuevoPrecio) {
        int cantidadActualizada = divisaRepository.actualizarPrecioCambioYMontoFinal(id, nuevoPrecio);
        if (cantidadActualizada != 1) {
            throw new RuntimeException("No se pudo actualizar la Divisa con id: " + id);
        }
    }

    @Override
    public String calculoCambioOperacion(DivisaDto divisaDto) {
        CalculoDecambio calculoDecambio = new CalculoDecambio();
        Divisa divisa = divisaMapper.toDivisa(divisaDto);

        String resultado = calculoDecambio.cambio(divisa);

        try {
            double montoFinal = Double.parseDouble(resultado);
            divisa.setMontoFinal(montoFinal);
        } catch (NumberFormatException e) {

            throw new IllegalStateException("El resultado del cálculo no es un número válido", e);
        }

        divisaRepository.save(divisa);

        return resultado;
    }


}
