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


import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        DivisaDto divisaDto1 = new DivisaDto(1, "Euros", "Soles",
                47.0, 3.2, 150.4);
        DivisaDto divisaDto2 = new DivisaDto(2, "Dolares", "Soles",
                47.0, 3.5, 164.5);
        divisaDtoList = Arrays.asList(divisaDto1, divisaDto2);


        when(divisaService.getDivisas()).thenReturn(divisaDtoList);


    }

    private List<Divisa> divisaList;
    private List<DivisaDto> divisaDtoList;

    @Test
    public void testCambioExitoso() throws Exception {
        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMonto(47.0);
        divisaDto.setPrecioDeCambio(3.2);


        String resultadoCambio = "150.4";
        when(divisaService.calculoCambioOperacion(any(DivisaDto.class))).thenReturn(resultadoCambio);


        mockMvc.perform(post("/api/cambio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divisaDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monedaOrigen").value("Dolares"))
                .andExpect(jsonPath("$.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.resultado").value(150.4))
                .andExpect(jsonPath("$.comentario")
                        .value("El cambio total de Dolares a Soles es 150.4 Soles"));


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
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message")
                        .value("El resultado del cálculo no es un número válido"))
                .andExpect(jsonPath("$.status").value(500));


        verify(divisaService, times(1)).calculoCambioOperacion(any(DivisaDto.class));
    }

    @Test
    public void testCambioConExcepcionGenerica() throws Exception {

        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMonto(47.0);


        when(divisaService.calculoCambioOperacion(any(DivisaDto.class)))
                .thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(post("/api/cambio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(divisaDto)))
                .andExpect(status().isInternalServerError());

        verify(divisaService, times(1)).calculoCambioOperacion(any(DivisaDto.class));
    }

    @Test
    public void testGetDivisaById_OK() throws Exception {
        DivisaDto divisaResponse = new DivisaDto();
        divisaResponse.setId(1);
        divisaResponse.setMonedaOrigen("Dolares");
        divisaResponse.setMonedaDestino("Soles");
        divisaResponse.setMonto(47.0);
        divisaResponse.setPrecioDeCambio(3.5);
        divisaResponse.setMontoFinal(164.5);

        when(divisaService.getDivisaById(1)).thenReturn(divisaResponse);

        mockMvc.perform(get("/api/divisa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.monedaOrigen").value("Dolares"))
                .andExpect(jsonPath("$.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.monto").value(47.0))
                .andExpect(jsonPath("$.precioDeCambio").value(3.5))
                .andExpect(jsonPath("$.montoFinal").value(164.5));

        verify(divisaService, times(1)).getDivisaById(1);
    }

    @Test
    public void testGetDivisaById_Error() throws Exception {

        when(divisaService.getDivisaById(2)).thenThrow(new NoSuchElementException("No value present"));

        mockMvc.perform(get("/api/divisa/2"))
                .andExpect(status().isNotFound()) 
                .andExpect(jsonPath("$.error").value("Server Error"))
                .andExpect(jsonPath("$.message").value("No value present"))
                .andExpect(jsonPath("$.status").value(404));

        verify(divisaService, times(1)).getDivisaById(2);
    }


    @Test
    public void testGetDivisas() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/divisas"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        System.out.println("Respuesta real de MockMvc: " + content);

        assertThat(content).isEqualTo(objectMapper.writeValueAsString(divisaDtoList));

        verify(divisaService).getDivisas();
    }

    @Test
    public void testDeleteById() throws Exception {

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setComentario("Divisa fue eliminada");

        when(divisaService.deleteDivisaById(1)).thenReturn(responseGenerico.getComentario());

        mockMvc.perform(delete("/api/divisa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comentario").value("Divisa fue eliminada"));

        verify(divisaService, times(1)).deleteDivisaById(1);
    }

    @Test
    public void testDeleteById_Error() throws Exception {

        ResponseGenerico responseGenerico = new ResponseGenerico();
        responseGenerico.setComentario("Divisa no existe");

        when(divisaService.deleteDivisaById(1)).thenReturn(responseGenerico.getComentario());

        mockMvc.perform(delete("/api/divisa/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.comentario").value("Divisa no existe"));

        verify(divisaService, times(1)).deleteDivisaById(1);
    }

    @Test
    public void testActualizarPrecioCambio_OK() throws Exception {
        Integer id = 1;
        Double nuevoTipoDeCambio = 3.5;

        DivisaDto divisaDto = new DivisaDto();
        divisaDto.setMonedaOrigen("Dolares");
        divisaDto.setMonedaDestino("Soles");
        divisaDto.setMontoFinal(263.2);

        when(divisaService.getDivisaById(id)).thenReturn(divisaDto);

        mockMvc.perform(post("/api/actualizarTipoDeCambio")
                        .param("id", String.valueOf(id))
                        .param("nuevoTipoDeCambio", String.valueOf(nuevoTipoDeCambio))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monedaOrigen").value("Dolares"))
                .andExpect(jsonPath("$.monedaDestino").value("Soles"))
                .andExpect(jsonPath("$.resultado").value(263.2))
                .andExpect(jsonPath("$.comentario").value("Divisa actualizada correctamente"));
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
                .andExpect(jsonPath("$.error").value("Divisa Error"))
                .andExpect(jsonPath("$.message").value("Por favor revisa los datos ingresados."))
                .andExpect(jsonPath("$.status").value(400));
    }

}





