package co.com.crediya.usecase.createsolicitud;

import co.com.crediya.model.loantype.gateways.LoanTypeRepository;
import co.com.crediya.model.solicitud.Solicitud;
import co.com.crediya.model.solicitud.gateways.SolicitudRepository;
import co.com.crediya.model.status.gateways.StatusRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CreateSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;

    /**
     * Crea y guarda una nueva solicitud.
     *
     * @param solicitud La solicitud a crear, con los datos iniciales.
     * @return Un Mono que emite la solicitud creada y persistida.
     */
    public Mono<Solicitud> create(Solicitud solicitud) {
        // En un caso de uso real, aquí iría la lógica de negocio.
        // Por ejemplo, validación de montos, reglas de negocio, etc.
        // Simulamos una validación simple y asignamos un estado.

        // Simplemente guardamos la solicitud sin validación compleja por ahora.
        Solicitud nuevaSolicitud = solicitud.toBuilder()
                .idStatus(1L) // Asume que 1L es el ID para el estado "PENDIENTE"
                .build();

        return solicitudRepository.save(nuevaSolicitud);
    }

}
