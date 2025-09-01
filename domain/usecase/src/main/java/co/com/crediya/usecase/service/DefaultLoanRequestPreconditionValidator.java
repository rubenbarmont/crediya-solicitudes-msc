package co.com.crediya.usecase.service;

import co.com.crediya.model.loanrequest.LoanRequest;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.model.loantype.exceptions.LoanTypeNotFoundException ;
import co.com.crediya.model.loanrequest.exceptions.UserNotFoundException ;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class DefaultLoanRequestPreconditionValidator implements LoanRequestPreconditionValidator {

    private final LoanTypeRepository loanTypeRepository;
    private final UserGateway userGateway;

    @Override
    public Mono<LoanRequest> validate(LoanRequest loanRequest) {
        return Mono.zip(
                        loanTypeRepository.findById(loanRequest.getIdLoanType())
                                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException("El tipo de préstamo seleccionado no existe."))),
                        userGateway.existsByEmail(loanRequest.getEmail())
                )
                // 1. Filtra el resultado. El flujo solo continúa si la tupla cumple la condición (si T2 es 'true').
                .filter(validationResult -> validationResult.getT2())
                // 2. Si el filtro no pasó (porque T2 era 'false'), el Mono se vacía.
                //    Aquí lo interceptamos y lanzamos el error de usuario no encontrado.
                .switchIfEmpty(Mono.error(new UserNotFoundException("El usuario con el email proporcionado no está registrado.")))
                // 3. Si el filtro pasó, ya no necesitamos la tupla. Descartamos su valor y devolvemos la solicitud original.
                .thenReturn(loanRequest);
    }
}
