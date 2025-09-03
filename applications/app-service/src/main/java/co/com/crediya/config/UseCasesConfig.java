package co.com.crediya.config;

import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.command.createloan.CreateLoanUseCase;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.LoanValidator;
import co.com.crediya.usecase.service.StandardLoanValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

        @Bean
        public LoanValidator loanValidator(
                LoanTypeRepository loanTypeRepository,
                UserGateway userGateway) {
                return new StandardLoanValidator(loanTypeRepository, userGateway);
        }

        @Bean
        public CreateLoanUseCase createLoanRequestUseCase(
                LoanValidator validator,
                LoanRepository loanRepository,
                StatusRepository statusRepository) {
                return new CreateLoanUseCase(validator, loanRepository, statusRepository);
        }
}
