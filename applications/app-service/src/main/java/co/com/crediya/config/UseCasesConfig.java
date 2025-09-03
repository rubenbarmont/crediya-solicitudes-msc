package co.com.crediya.config;

import co.com.crediya.model.loanrequest.gateways.LoanRequestRepository;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.command.createloanrequest.CreateLoanRequestUseCase;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.LoanRequestValidator;
import co.com.crediya.usecase.service.StandardLoanRequestValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public LoanRequestValidator loanRequestValidator(
                LoanTypeRepository loanTypeRepository,
                UserGateway userGateway) {
                return new StandardLoanRequestValidator(loanTypeRepository, userGateway);
        }

        @Bean
        public CreateLoanRequestUseCase createLoanRequestUseCase(
                LoanRequestValidator validator,
                LoanRequestRepository loanRequestRepository,
                StatusRepository statusRepository) {
                return new CreateLoanRequestUseCase(validator, loanRequestRepository, statusRepository);
        }
}
