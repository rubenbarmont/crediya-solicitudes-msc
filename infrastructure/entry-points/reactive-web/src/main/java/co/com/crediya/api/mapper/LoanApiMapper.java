package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loan.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LoanApiMapper {

    /**
     * Convierte un DTO de entrada a un objeto del modelo de dominio.
     * Se especifican explícitamente todos los mapeos para mayor robustez.
     */
    @Mapping(target = "idLoan", ignore = true)
    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "identityDocument", source = "identityDocument")
    @Mapping(target = "amount", source = "amount")
    @Mapping(target = "term", source = "term")
    @Mapping(target = "idLoanType", source = "idLoanType")
    Loan toDomain(LoanRequestDTO dto);
}
