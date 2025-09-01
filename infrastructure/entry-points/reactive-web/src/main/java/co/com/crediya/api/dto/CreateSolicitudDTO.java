package co.com.crediya.api.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateSolicitudDTO {

    private Long idUserDocument;
    private BigDecimal amount;
    private Long term;
    private String LoanTypeName;


}
