package com.cambio.divisas.controller;

import com.cambio.divisas.domain.request.DivisaDto;
import com.cambio.divisas.domain.response.DivisaResponse;
import com.cambio.divisas.domain.response.ResponseGeneralDto;
import com.cambio.divisas.domain.response.ResponseGenerico;
import com.cambio.divisas.exception.DivisaException;
import com.cambio.divisas.exception.ErrorResponse;
import com.cambio.divisas.service.DivisaServiceImpl;
import com.cambio.divisas.util.Contants;
import com.cambio.divisas.util.DivisaAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ResponseGeneralDto> cambio(@RequestBody DivisaDto divisaDto) {
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

            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_200, HttpStatus.OK.value(),
                    Contants.messageProcessOK,response),HttpStatus.OK);

        } catch (NumberFormatException e) {

            throw new DivisaException("Por favor revisa los datos ingresados.");
        } catch (DivisaException e) {

            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_500,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Contants.messageProcessError,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_500,
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    Contants.messageProcessError,e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("divisa/{id}")
    public ResponseEntity<ResponseGeneralDto> getDivisaById(@PathVariable Integer id) {
        try {
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_200, HttpStatus.OK.value(),
                    Contants.messageProcessOK,divisaService.getDivisaById(id)),HttpStatus.OK);

        } catch (DivisaException e) {
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_404,
                    HttpStatus.NOT_FOUND.value(),
                    Contants.messageProcessNotFound,e.getMessage()),HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_404,
                    HttpStatus.NOT_FOUND.value(),
                    Contants.messageProcessNotFound,e.getMessage()),HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/divisas")
    public ResponseEntity<ResponseGeneralDto> getAllDivisas() {
        List<DivisaDto> divisaFound= divisaService.getDivisas();
        return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_200, HttpStatus.OK.value(),
                Contants.messageProcessOK,divisaFound),HttpStatus.OK);

    }


    @DeleteMapping("divisa/{id}")
    public ResponseEntity<ResponseGeneralDto> deleteDivisa(@PathVariable Integer id) {
        try {
            ResponseGenerico response = new ResponseGenerico();
            response.setComentario(divisaService.deleteDivisaById(id));
            if (response.getComentario().equals("Divisa no existe")) {

                return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_404,
                        HttpStatus.NOT_FOUND.value(),
                        Contants.messageProcessNotFound,response),HttpStatus.NOT_FOUND);

            } else {
                return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_200, HttpStatus.OK.value(),
                        Contants.messageProcessOK,response),HttpStatus.OK);
            }
        } catch (DivisaException e) {

            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_404,
                    HttpStatus.NOT_FOUND.value(),
                    Contants.messageProcessNotFound,e.getMessage()),HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_404,
                    HttpStatus.NOT_FOUND.value(),
                    Contants.messageProcessNotFound,e.getMessage()),HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/actualizarTipoDeCambio")
    public ResponseEntity<ResponseGeneralDto> actualizarPrecioCambio(
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
            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_200, HttpStatus.OK.value(),
                    Contants.messageProcessOK,response),HttpStatus.OK);
        } catch (RuntimeException e) {

            return new ResponseEntity<>(DivisaAdapter.responseGeneral(Contants.HTTP_400,
                    HttpStatus.BAD_REQUEST.value(),
                    Contants.messageProcessBadRequest,"Por favor revisa los datos ingresados."),HttpStatus.BAD_REQUEST);


        } catch (Exception e) {
            throw new RuntimeException("Error en el servidor: " + e.getMessage());
        }
    }
}
