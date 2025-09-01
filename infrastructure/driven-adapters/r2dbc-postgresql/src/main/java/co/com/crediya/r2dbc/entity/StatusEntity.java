package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("estados")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class StatusEntity {
    @Id
    @Column("id_estado")
    private Long idStatus;
    @Column("nombre")
    private String name;
    @Column("descripcion")
    private String description;
}
