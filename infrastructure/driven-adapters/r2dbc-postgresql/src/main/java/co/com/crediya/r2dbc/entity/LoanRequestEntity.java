package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanRequestEntity {
    @Id
    @Column("id_solicitud")
    private Long idSolicitud;
    private BigDecimal amount;
    private Long term;
    private String email;
    @Column("id_estado")
    private Long idStatus;
    @Column("id_tipo_prestamo")
    private Long idLoanType;
}
