package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.LoanRequestDTO;
import co.com.crediya.model.loanrequest.LoanRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LoanRequestApiMapper {
    @Mapping(target = "idLoanRequest", ignore = true)
    @Mapping(target = "idStatus", ignore = true) // <-- CORREGIDO: de "status" a "idStatus"
    LoanRequest toDomain(LoanRequestDTO dto);
}
