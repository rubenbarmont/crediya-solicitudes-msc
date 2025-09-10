package co.com.crediya.model.loan.exceptions;

import co.com.crediya.model.exceptions.BusinessException;

public class LoanCreationForbiddenException extends BusinessException {
    public LoanCreationForbiddenException() {
        super("Un cliente solo puede crear solicitudes de préstamo para sí mismo.");
    }
}
