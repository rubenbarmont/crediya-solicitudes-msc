package co.com.crediya.model.user;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long identityDocument;
    private String email;
    private BigDecimal baseSalary;
}
