package co.com.crediya.usecase.service;

public final class LoanConstants {

    private LoanConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Nombres de campos
    public static final String FIELD_IDENTITY_DOCUMENT = "documento de identidad";
    public static final String FIELD_AMOUNT = "monto";
    public static final String FIELD_TERM = "plazo";
    public static final String FIELD_LOAN_TYPE_ID = "tipo de préstamo";

    // Mensajes de error
    public static final String ERROR_FIELD_REQUIRED = "El campo '%s' es obligatorio.";
    public static final String ERROR_AMOUNT_POSITIVE = "El monto debe ser un número positivo.";
    public static final String ERROR_TERM_MINIMUM = "El plazo mínimo es de 1 mes.";
}
