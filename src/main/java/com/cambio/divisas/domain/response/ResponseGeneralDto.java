package com.cambio.divisas.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGeneralDto {
    private String code;
    private Integer status;
    private String comment;
    private Object data;




}
