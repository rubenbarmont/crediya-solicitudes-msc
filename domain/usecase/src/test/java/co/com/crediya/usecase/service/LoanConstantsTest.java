package co.com.crediya.usecase.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class LoanConstantsTest {

    @Test
    void constructorShouldThrowException() throws Exception {
        Constructor<LoanConstants> constructor = LoanConstants.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        InvocationTargetException exception =
                assertThrows(InvocationTargetException.class, constructor::newInstance);

        // El cause real debe ser tu IllegalStateException
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalStateException);
        assertEquals("Utility class", cause.getMessage());
    }


    @Test
    void shouldAccessAllConstants() {
        assertEquals("documento de identidad", LoanConstants.FIELD_IDENTITY_DOCUMENT);
        assertEquals("monto", LoanConstants.FIELD_AMOUNT);
        assertEquals("plazo", LoanConstants.FIELD_TERM);
        assertEquals("tipo de préstamo", LoanConstants.FIELD_LOAN_TYPE_ID);

        assertTrue(LoanConstants.ERROR_FIELD_REQUIRED.contains("obligatorio"));
        assertTrue(LoanConstants.ERROR_AMOUNT_POSITIVE.contains("positivo"));
        assertTrue(LoanConstants.ERROR_TERM_MINIMUM.contains("mínimo"));
    }
}
