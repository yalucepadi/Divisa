package com.cambio.divisas.controllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.cambio.divisas.controller.DivisaController;
import com.cambio.divisas.domain.Divisa;
import com.cambio.divisas.domain.request.DivisaDto;
import com.cambio.divisas.domain.response.ResponseGenerico;
import com.cambio.divisas.exception.DivisaException;
import com.cambio.divisas.repository.DivisaRepository;
import com.cambio.divisas.service.DivisaServiceImpl;
import com.cambio.divisas.util.MapperDivisa;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;


import java.util.*;

@WebMvcTest(DivisaController.class)
public class DivisaControllerTest {


    @Autowired
    private DivisaController divisaController;
    @Mock
    private DivisaRepository divisaRepository;
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DivisaServiceImpl divisaService;

    @MockBean
    private MapperDivisa divisaMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Divisa> divisaList;
    private List<DivisaDto> divisaDtoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        DivisaDto divisaDto1 = new DivisaDto(1, "Euros", "Soles", 47.0
                , 4.3, 202.1);
        DivisaDto divisaDto2 = new DivisaDto(2, "Dolares", "Soles", 47.0
                , 4.3, 202.1);
        divisaDtoList = Arrays.asList(divisaDto1, divisaDto2);


        when(divisaService.getDivisas()).thenReturn(divisaDtoList);


    }



    @Test
    public void testCambioExitoso() throws Exception {

        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Euros");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMonto(63.2);
        divisaDto.setPrecioDeCambio(3.2);


        String resultadoCambio = "202.1";
        when(divisaService.calculoCambioOperacion(any(DivisaDto.class))).thenReturn(resultadoCambio);


        mockMvc.perform(post("/api/cambio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divisaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.comment").value("Request successful"))
                .andExpect(jsonPath("$.data.monedaOrigen").value("Euros"))
                .andExpect(jsonPath("$.data.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.data.resultado").value(202.1))
                .andExpect(jsonPath("$.data.comentario")
                        .value("El cambio total de Euros a Soles es 202.1 Soles"));


        verify(divisaService, times(1)).calculoCambioOperacion(any(DivisaDto.class));
    }


    @Test
    public void testCambioConResultadoInvalido() throws Exception {

        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMonto(47.0);
        divisaDto.setPrecioDeCambio(0.0);

        when(divisaService.calculoCambioOperacion(any(DivisaDto.class))).thenReturn("0.0");

        mockMvc.perform(post("/api/cambio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divisaDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.comment").value("Internal Server Error"))
                .andExpect(jsonPath("$.data")
                        .value("El resultado del cálculo no es un número válido"));

        verify(divisaService, times(1)).calculoCambioOperacion(any(DivisaDto.class));
    }


    @Test
    public void testCambioConExcepcionGenerica() throws Exception {


        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMonto(47.0);

        when(divisaService.calculoCambioOperacion(any(DivisaDto.class)))
                .thenThrow(new RuntimeException("Cannot invoke \"java.lang.Double.doubleValue()\"" +
                        " because the return value of" +
                        " \"com.cambio.divisas.domain.Divisa.getPrecioDeCambio()\" is null"));


        mockMvc.perform(post("/api/cambio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divisaDto)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("500"))
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.comment").value("Internal Server Error"))
                .andExpect(jsonPath("$.data")
                        .value("Cannot invoke \"java.lang.Double.doubleValue()\" " +
                                "because the return value of " +
                                "\"com.cambio.divisas.domain.Divisa.getPrecioDeCambio()\" is null"));


        verify(divisaService, times(1)).calculoCambioOperacion(any(DivisaDto.class));
    }

    @Test
    public void testGetDivisaById_OK() throws Exception {

        DivisaDto divisaResponse = new DivisaDto();
        divisaResponse.setId(1);
        divisaResponse.setMonedaOrigen("Euros");
        divisaResponse.setMonedaDestino("Soles");
        divisaResponse.setMonto(47.0);
        divisaResponse.setPrecioDeCambio(4.3);
        divisaResponse.setMontoFinal(202.1);


        when(divisaService.getDivisaById(1)).thenReturn(divisaResponse);

        mockMvc.perform(get("/api/divisa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.comment").value("Request successful"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.monedaOrigen").value("Euros"))
                .andExpect(jsonPath("$.data.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.data.monto").value(47.0))
                .andExpect(jsonPath("$.data.precioDeCambio").value(4.3))
                .andExpect(jsonPath("$.data.montoFinal").value(202.1));


        verify(divisaService, times(1)).getDivisaById(1);
    }


    @Test
    public void testGetDivisaById_Error() throws Exception {

        when(divisaService.getDivisaById(2)).thenThrow(new NoSuchElementException("No value present"));

        mockMvc.perform(get("/api/divisa/2"))
                .andExpect(status().isNotFound()) // Verificar que el estatus sea 404
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.comment").value("Not Found"))
                .andExpect(jsonPath("$.data").value("No value present"));

        verify(divisaService, times(1)).getDivisaById(2);
    }



    @Test
    public void testGetDivisas() throws Exception {
        Map<String, Object> expectedResponse = new LinkedHashMap<>();
        expectedResponse.put("code", "200");
        expectedResponse.put("status", 200);
        expectedResponse.put("comment", "Request successful");
        expectedResponse.put("data", divisaDtoList);

        MvcResult mvcResult = mockMvc.perform(get("/api/divisas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Respuesta real de MockMvc: " + content);

        assertThat(content).isEqualTo(objectMapper.writeValueAsString(expectedResponse));

        verify(divisaService).getDivisas();
    }


    @Test
    public void testDeleteById() throws Exception {

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setComentario("Divisa fue eliminada");

        when(divisaService.deleteDivisaById(1)).thenReturn(responseGenerico.getComentario());

        mockMvc.perform(delete("/api/divisa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.comment").value("Request successful"))
                .andExpect(jsonPath("$.data.comentario").value("Divisa fue eliminada"));

        verify(divisaService, times(1)).deleteDivisaById(1);
    }


    @Test
    public void testDeleteById_Error() throws Exception {

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setComentario("Divisa no existe");
        when(divisaService.deleteDivisaById(1)).thenThrow(new NoSuchElementException("Divisa no existe"));

        mockMvc.perform(delete("/api/divisa/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.comment").value("Not Found"))
                .andExpect(jsonPath("$.data").value("Divisa no existe"));

        verify(divisaService, times(1)).deleteDivisaById(1);
    }


    @Test
    public void testActualizarPrecioCambio_OK() throws Exception {
        Integer id = 1;
        Double nuevoTipoDeCambio = 3.5;

        Divisa divisa = new Divisa();
        divisa.setId(id);
        divisa.setPrecioDeCambio(nuevoTipoDeCambio);

        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMontoFinal(164.5);


        when(divisaService.getDivisaById(id)).thenReturn(divisaDto);


        doNothing().when(divisaService).actualizarDivisa(id, nuevoTipoDeCambio);
        when(divisaMapper.toDto(divisa)).thenReturn(divisaDto);


        mockMvc.perform(post("/api/actualizarTipoDeCambio")
                        .param("id", String.valueOf(id))
                        .param("nuevoTipoDeCambio", String.valueOf(nuevoTipoDeCambio))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.comment").value("Request successful"))
                .andExpect(jsonPath("$.data.monedaOrigen").value("Dolares"))
                .andExpect(jsonPath("$.data.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.data.resultado").value(164.5))
                .andExpect(jsonPath("$.data.comentario").value("Divisa actualizada correctamente"));
    }


    @Test
    public void testActualizarPrecioCambio_Error() throws Exception {
        Integer id = 1;
        Double nuevoTipoDeCambio = 3.5;


        doThrow(new DivisaException("Por favor revisa los datos ingresados."))
                .when(divisaService).actualizarDivisa(id, nuevoTipoDeCambio);


        mockMvc.perform(post("/api/actualizarTipoDeCambio")
                        .param("id", String.valueOf(id))
                        .param("nuevoTipoDeCambio", String.valueOf(nuevoTipoDeCambio))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.comment").value("Bad Request"))
                .andExpect(jsonPath("$.data").value("Por favor revisa los datos ingresados."));
    }


}





