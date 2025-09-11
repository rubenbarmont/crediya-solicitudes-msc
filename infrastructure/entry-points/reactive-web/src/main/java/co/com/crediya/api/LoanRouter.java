package co.com.crediya.api;

import co.com.crediya.api.dto.ErrorResponseDTO;
import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loan.Loan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.annotations.RouterOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class LoanRouter {

    @Bean
    @RouterOperation(
            path = "/api/v1/solicitud",
            method = RequestMethod.POST,
            beanClass = LoanHandler.class,
            beanMethod = "createLoanRequest",
            operation = @Operation(
                    operationId = "createLoanRequest",
                    summary = "Registrar una nueva solicitud de préstamo (Requiere rol CLIENTE)",
                    tags = {"Solicitudes de Préstamo"},
                    security = @SecurityRequirement(name = "bearerAuth"),
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = LoanRequestDTO.class))),
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente.", content = @Content(schema = @Schema(implementation = Loan.class))),
                            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o regla de negocio no cumplida.", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                            @ApiResponse(responseCode = "401", description = "No autenticado. Token JWT no proporcionado, inválido o expirado.", content = @Content(schema = @Schema(implementation = Void.class))),
                            @ApiResponse(responseCode = "403", description = "Acceso denegado. El usuario no tiene el rol 'CLIENTE'.", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
                    }
            )
    )
    public RouterFunction<ServerResponse> loanRequestRouterFunction(LoanHandler handler) {
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::createLoanRequest);
    }
}
