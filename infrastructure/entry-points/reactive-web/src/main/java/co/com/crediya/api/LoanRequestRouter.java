package co.com.crediya.api;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loanrequest.LoanRequest;
import io.swagger.v3.oas.annotations.Operation;
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
public class LoanRequestRouter {
    @Bean
    @RouterOperation(
            path = "/api/v1/solicitudes",
            method = RequestMethod.POST,
            beanClass = LoanRequestHandler.class,
            beanMethod = "createLoanRequest",
            operation = @Operation(
                    operationId = "createLoanRequest",
                    summary = "Registrar una nueva solicitud de préstamo",
                    tags = {"Solicitudes de Préstamo"},
                    requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = LoanRequestDTO.class))),
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Solicitud creada exitosamente.", content = @Content(schema = @Schema(implementation = LoanRequest.class))),
                            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o precondición no cumplida (ej. usuario no existe)."),
                            @ApiResponse(responseCode = "500", description = "Error interno del servidor (ej. estado inicial no configurado).")
                    }
            )
    )
    public RouterFunction<ServerResponse> loanRequestRouterFunction(LoanRequestHandler handler) {
        return route(POST("/api/v1/solicitudes").and(accept(MediaType.APPLICATION_JSON)), handler::createLoanRequest);
    }
}
