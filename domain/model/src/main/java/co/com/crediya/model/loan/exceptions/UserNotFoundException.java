package co.com.crediya.model.loan.exceptions;

import co.com.crediya.model.exceptions.BusinessException;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(Long identityDocument) {
        super(String.format("El usuario con documento de identidad '%d' no se encuentra registrado en el sistema.", identityDocument));
    }
}
