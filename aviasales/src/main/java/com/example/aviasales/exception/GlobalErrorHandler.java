package com.example.aviasales.exception;

import com.example.aviasales.dto.ErrorDTO;
import com.example.aviasales.exception.not_found.ResourceNotFoundException;
import com.example.aviasales.exception.not_match.ResourceNotMatchException;
import com.example.aviasales.util.enums.Code;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@ResponseBody
public class GlobalErrorHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDTO handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.RESOURCE_NOT_FOUND, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(ResourceNotMatchException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleNotMatchesException(ResourceNotMatchException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.NOT_MATCHES, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }
    @ExceptionHandler(NotEnoughSeatsInAircraftException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleNotEnoughSeatsInAircraftException(NotEnoughSeatsInAircraftException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.IMPOSSIBLE_OPERATION, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(NoAdultsForFlightException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleNoAdultsForFlightException(NoAdultsForFlightException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.IMPOSSIBLE_OPERATION, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(MailException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleMailException(MailException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.MAIL_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUserAlreadyExistsException(UserAlreadyExistsException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.AUTH_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(FlightWithTheSameAirportsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleFlightWithTheSameAirportsException(FlightWithTheSameAirportsException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.IMPOSSIBLE_OPERATION, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(DepartureTimeAfterArrivalTimeException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleDepartureTimeAfterArrivalTimeException(DepartureTimeAfterArrivalTimeException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.IMPOSSIBLE_OPERATION, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleTransactionException(TransactionException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.TRANSACTION_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }
}
