package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loanrequest.LoanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LoanRequestApiMapper {

    /**
     * Convierte un DTO de entrada a un objeto del modelo de dominio.
     * Se especifican explícitamente todos los mapeos para mayor robustez.
     */
    @Mapping(target = "idLoanRequest", ignore = true)
    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "identityDocument", source = "identityDocument") // Mapeo explícito
    @Mapping(target = "amount", source = "amount")                   // Mapeo explícito
    @Mapping(target = "term", source = "term")                       // Mapeo explícito
    @Mapping(target = "idLoanType", source = "idLoanType")             // Mapeo explícito
    LoanRequest toDomain(LoanRequestDTO dto);
}
