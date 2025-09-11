package co.com.crediya.config;

import co.com.crediya.model.loan.gateways.LoanRepository;
import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import co.com.crediya.usecase.command.createloan.CreateLoanUseCase;
import co.com.crediya.usecase.gateways.UserGateway;
import co.com.crediya.usecase.service.LoanValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class UseCasesConfigTest {

    private AnnotationConfigApplicationContext context;

    @BeforeEach
    void setUp() {
        // Arrange: Creamos el contexto usando nuestra configuración de prueba
        context = new AnnotationConfigApplicationContext(TestConfig.class);
    }

    @AfterEach
    void tearDown() {
        // Limpiamos el contexto después de cada test
        context.close();
    }

    @Test
    void shouldCreateCreateLoanUseCaseBean() {
        // Act & Assert
        CreateLoanUseCase useCase = context.getBean(CreateLoanUseCase.class);
        assertNotNull(useCase, "El bean de CreateLoanUseCase no debería ser nulo.");
    }

    @Test
    void shouldCreateLoanValidatorBean() {
        // Act & Assert
        LoanValidator validator = context.getBean(LoanValidator.class);
        assertNotNull(validator, "El bean de LoanValidator no debería ser nulo.");
    }


    // --- Nuestra Configuración de Prueba anidada ---
    @Configuration
    @Import(UseCasesConfig.class) // Importamos la configuración real de la aplicación
    static class TestConfig {

        // Creamos Mocks para TODAS las dependencias que los UseCases necesitan.
        // Spring usará estos mocks para satisfacer las dependencias.

        @Bean
        public LoanRepository loanRepository() {
            return Mockito.mock(LoanRepository.class);
        }

        @Bean
        public LoanTypeRepository loanTypeRepository() {
            return Mockito.mock(LoanTypeRepository.class);
        }

        @Bean
        public StatusRepository statusRepository() {
            return Mockito.mock(StatusRepository.class);
        }

        @Bean
        public UserGateway userGateway() {
            return Mockito.mock(UserGateway.class);
        }
    }
}