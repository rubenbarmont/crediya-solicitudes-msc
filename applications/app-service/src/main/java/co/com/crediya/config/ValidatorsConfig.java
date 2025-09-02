package co.com.crediya.config;


import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.StandardLoanRequestValidator;
import co.com.crediya.usecase.service.LoanRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorsConfig {

    // Bean del validador de precondiciones de solicitudes de préstamo
    @Bean
    public LoanRequestValidator loanRequestPreconditionValidator(
            LoanTypeRepository loanTypeRepository,
            UserGateway userGateway
    ) {
        return new StandardLoanRequestValidator(loanTypeRepository, userGateway);
    }
}
