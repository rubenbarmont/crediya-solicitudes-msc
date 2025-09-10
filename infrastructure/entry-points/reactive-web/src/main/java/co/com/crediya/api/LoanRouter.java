package co.com.crediya.api;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loan.Loan;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
                    summary = "Registrar una nueva solicitud de préstamo",
                    tags = {"Solicitudes de Préstamo"},
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = LoanRequestDTO.class))),
                    responses = {
                            @ApiResponse(
                                    responseCode = "201",
                                    description = "Solicitud creada exitosamente.",
                                    content = @Content(schema = @Schema(implementation = Loan.class))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Datos de entrada inválidos. Ocurre cuando faltan campos o tienen un formato incorrecto.",
                                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
                            ),
                            @ApiResponse(
                                    responseCode = "400",
                                    description = "Regla de negocio no cumplida. Ocurre cuando el usuario o el tipo de préstamo no existen.",
                                    content = @Content(schema = @Schema(implementation = String.class))
                            ),
                            @ApiResponse(
                                    responseCode = "500",
                                    description = "Error interno del servidor. Generalmente causado por una configuración faltante, como el estado inicial no encontrado en la base de datos.",
                                    content = @Content(schema = @Schema(implementation = String.class))
                            )
                    }
            )
    )
    public RouterFunction<ServerResponse> loanRequestRouterFunction(LoanHandler handler) {
        return route(POST("/api/v1/solicitud").and(accept(MediaType.APPLICATION_JSON)), handler::createLoanRequest);
    }
}
