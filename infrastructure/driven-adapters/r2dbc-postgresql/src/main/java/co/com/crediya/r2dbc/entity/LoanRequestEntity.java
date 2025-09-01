package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loan_request")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanRequestEntity {
    @Id
    @Column("id_loan_request")
    private Long idLoanRequest;
    private BigDecimal amount;
    private Long term;
    private String email;
    @Column("id_status")
    private Long idStatus;
    @Column("id_loan_type")
    private Long idLoanType;
}
