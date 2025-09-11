package co.com.crediya.consumer.dto;

import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponseDTO {
    private Long identityDocument;
    private String email;
    private BigDecimal baseSalary;
}
