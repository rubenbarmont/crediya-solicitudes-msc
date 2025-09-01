package co.com.crediya.config;

import co.com.crediya.model.loanrequest.gateways.LoanRequestRepository;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.command.createloanrequest.CreateLoanRequestUseCase;
import co.com.crediya.usecase.command.usergateway.UserGateway;
import co.com.crediya.usecase.service.DefaultLoanRequestPreconditionValidator;
import co.com.crediya.usecase.service.LoanRequestPreconditionValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class UseCasesConfig {

        // 1. Creamos el bean del validador de precondiciones.
        @Bean
        public LoanRequestPreconditionValidator loanRequestPreconditionValidator(
                // Spring inyectará aquí los adaptadores que implementan estas interfaces (puertos).
                LoanTypeRepository loanTypeRepository,
                UserGateway userGateway) {
                return new DefaultLoanRequestPreconditionValidator(loanTypeRepository, userGateway);
        }

        // 2. Creamos el bean del caso de uso principal.
        @Bean
        public CreateLoanRequestUseCase createSolicitudUseCase(
                // Spring inyecta el bean que creamos arriba y los adaptadores necesarios.
                LoanRequestPreconditionValidator preconditionValidator,
                LoanRequestRepository loanRequestRepository,
                StatusRepository statusRepository) {
                return new CreateLoanRequestUseCase(preconditionValidator, loanRequestRepository, statusRepository);
        }
}
