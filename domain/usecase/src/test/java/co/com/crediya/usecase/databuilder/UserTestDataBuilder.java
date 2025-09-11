package co.com.crediya.usecase.databuilder;

import co.com.crediya.model.user.User;
import java.math.BigDecimal;

public class UserTestDataBuilder {
    private Long identityDocument;
    private String email;

    public UserTestDataBuilder() {
        this.identityDocument = 12345L;
        this.email = "test@user.com";
    }

    public UserTestDataBuilder withIdentityDocument(Long identityDocument) {
        this.identityDocument = identityDocument;
        return this;
    }

    public User build() {
        return User.builder()
                .identityDocument(identityDocument)
                .email(email)
                .baseSalary(new BigDecimal("2000000"))
                .build();
    }
}
