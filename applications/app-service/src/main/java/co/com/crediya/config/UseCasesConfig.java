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
                LoanTypeRepository loanTypeRepository) { // <-- 1. Se elimina UserGateway de aquí
                return new StandardLoanValidator(loanTypeRepository); // <-- 2. Y se quita de la creación
        }

        @Bean
        public CreateLoanUseCase createLoanUseCase( // <-- 3. Renombrado para ser consistente
                                                    LoanValidator validator,
                                                    LoanRepository loanRepository,
                                                    StatusRepository statusRepository,
                                                    UserGateway userGateway) { // <-- 4. Se añade UserGateway aquí
                return new CreateLoanUseCase(validator, loanRepository, statusRepository, userGateway); // <-- 5. Y se pasa al constructor
        }
}
