package com.example.aviasales.exception;

import com.example.aviasales.dto.ErrorDTO;
import com.example.aviasales.util.enums.Code;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
@ResponseBody
public class JSONErrorHandler {

    @ExceptionHandler(UnrecognizedPropertyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleUnrecognizedPropertyException(UnrecognizedPropertyException ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.JSON_ERROR, new Date(), "Неидентифицированное поле в JSON.", request.getDescription(false));
        return errorDTO;
    }

    @ExceptionHandler({JsonParseException.class, JsonMappingException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDTO handleJsonException(Exception ex, WebRequest request) {
        ErrorDTO errorDTO = new ErrorDTO(
                Code.JSON_ERROR, new Date(), ex.getMessage(), request.getDescription(false));
        return errorDTO;
    }
}
