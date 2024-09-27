package com.cambio.divisas.controller;

import com.cambio.divisas.domain.request.DivisaDto;
import com.cambio.divisas.domain.response.DivisaResponse;
import com.cambio.divisas.domain.response.ResponseGenerico;
import com.cambio.divisas.exception.DivisaException;
import com.cambio.divisas.exception.ErrorResponse;
import com.cambio.divisas.service.DivisaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DivisaController {

    @Autowired
    DivisaServiceImpl divisaService;


    @PostMapping("/cambio")
    public ResponseEntity<?> cambio(@RequestBody DivisaDto divisaDto) {
        try {
            String resultadoCambio = divisaService.calculoCambioOperacion(divisaDto);

            double montoFinal = Double.parseDouble(resultadoCambio);
            if (montoFinal <= 0) {
                throw new DivisaException("El resultado del cálculo no es un número válido");
            }

            DivisaResponse response = new DivisaResponse();
            response.setMonedaOrigen(divisaDto.getMonedaOrigen());
            response.setMonedaDestino(divisaDto.getMonedaDestino());
            response.setResultado(montoFinal);
            response.setComentario("El cambio total de " + divisaDto.getMonedaOrigen() + " a "
                    + divisaDto.getMonedaDestino()
                    + " es " + montoFinal + " " + divisaDto.getMonedaDestino());

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {

            throw new DivisaException("Por favor revisa los datos ingresados.");
        } catch (DivisaException e) {

            return ResponseEntity.status(500).body(new ErrorResponse("Server Error", e.getMessage(), 500));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("Server Error",
                    "Error en el servidor: " + e.getMessage(), 500));
        }
    }

    @GetMapping("divisa/{id}")
    public ResponseEntity<?> getDivisaById(@PathVariable Integer id) {
        try {

            return ResponseEntity.ok(divisaService.getDivisaById(id));
        } catch (DivisaException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Not Found", e.getMessage(), 404));

        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Server Error", e.getMessage(), 404));
        }

    }

    @GetMapping("/divisas")
    public List<DivisaDto> getAllDivisas() {

        return divisaService.getDivisas();
    }


    @DeleteMapping("divisa/{id}")
    public ResponseEntity<?> deleteDivisa(@PathVariable Integer id) {
        try {
            ResponseGenerico response = new ResponseGenerico();
            response.setComentario(divisaService.deleteDivisaById(id));
            if (response.getComentario().equals("Divisa no existe")) {


                return new ResponseEntity(response, HttpStatusCode.valueOf(404));
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (DivisaException e) {

            return ResponseEntity.status(404).body(new ErrorResponse("Not Found",
                    divisaService.deleteDivisaById(id), 404));

        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ErrorResponse("Server Error",
                    divisaService.deleteDivisaById(id), 404));
        }
    }

    @PostMapping("/actualizarTipoDeCambio")
    public ResponseEntity<?> actualizarPrecioCambio(
            @RequestParam Integer id,
            @RequestParam Double nuevoTipoDeCambio) {
        try {


            divisaService.actualizarDivisa(id, nuevoTipoDeCambio);
            DivisaDto divisaDto = divisaService.getDivisaById(id);

            DivisaResponse response = new DivisaResponse();
            response.setMonedaOrigen(divisaDto.getMonedaOrigen());
            response.setMonedaDestino(divisaDto.getMonedaDestino());
            response.setResultado(divisaDto.getMontoFinal());
            response.setComentario("Divisa actualizada correctamente");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new DivisaException("Por favor revisa los datos ingresados.");

        } catch (Exception e) {
            throw new RuntimeException("Error en el servidor: " + e.getMessage());
        }
    }
}
