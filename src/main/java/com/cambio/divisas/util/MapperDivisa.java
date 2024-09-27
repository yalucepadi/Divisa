package com.cambio.divisas.util;

import com.cambio.divisas.domain.Divisa;
import com.cambio.divisas.domain.request.DivisaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")

public interface MapperDivisa {


    @Mapping(target = "montoFinal", source = "montoFinal")
    Divisa toDivisa(DivisaDto divisaDto);

    @Mapping(target = "montoFinal", source = "montoFinal")
    DivisaDto toDto(Divisa divisa);

    @Mapping(target = "montoFinal", source = "montoFinal")
    void updateDivisaFromDto(DivisaDto dto, @MappingTarget Divisa divisa);


}
