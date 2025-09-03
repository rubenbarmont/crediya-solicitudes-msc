package co.com.crediya.usecase.databuilder;

import co.com.crediya.model.status.Status;

public class StatusBuilder {

    private Long idStatus;
    private String name;
    private String description;

    public StatusBuilder() {
        this.idStatus = 1L;
        this.name = "Pendiente de revisión";
        this.description = "La solicitud está esperando la revisión de un asesor.";
    }

    public Status build() {
        return Status.builder()
                .idStatus(this.idStatus)
                .name(this.name)
                .description(this.description)
                .build();
    }
}
