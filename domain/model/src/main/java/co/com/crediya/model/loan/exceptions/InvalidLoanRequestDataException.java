package co.com.crediya.model.loan.exceptions;

import co.com.crediya.model.exceptions.BusinessException;

import java.util.List;

public class InvalidLoanRequestDataException extends BusinessException {

    private final List<String> errors;

    public InvalidLoanRequestDataException(List<String> errors) {
        // 1. Llamamos al constructor padre con un mensaje de error genérico.
        super("Los datos de la solicitud de préstamo son inválidos.");
        // 2. Guardamos la lista específica de errores.
        this.errors = errors;
    }

    /**
     * Permite al manejador de errores (Handler) recuperar la lista completa de mensajes de validación.
     * @return La lista de errores de validación.
     */
    public List<String> getErrors() {
        return errors;
    }
}

