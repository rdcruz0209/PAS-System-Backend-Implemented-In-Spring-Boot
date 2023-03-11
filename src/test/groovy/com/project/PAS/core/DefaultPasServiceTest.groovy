package com.project.PAS.core

import com.project.PAS.core.dto.pas.PasRequestDTO
import com.project.PAS.core.repository.PasRepository
import com.project.PAS.presentation.mock.PasMockData
import jakarta.persistence.criteria.CriteriaBuilder
import jakarta.persistence.criteria.CriteriaQuery
import jakarta.persistence.criteria.Predicate
import jakarta.persistence.criteria.Root
import org.springframework.data.jpa.domain.Specification
import org.springframework.lang.Nullable

import java.time.LocalDate

class DefaultPasServiceTest implements Specification{
    PasService pasService
    PasRepository pasRepository

    def setup(){
        pasRepository.Mock(PasRepository)
        pasService = new com.project.PAS.core.impl.DefaultPasService(pasRepository);
    }

    def "Should be able to getPasByAccountNumber given AccountNumber"(){
        when:
        def result = pasService.getPasByAccountNumber(1000L)
        then:
        1 * pasRepository.getPas(_) >> { args ->
            with(args[0] as Long){
                assert it == 1000L
            }
            PasMockData.generatePasResponseDTO()
        }
        result.getPasDTOvalues.accountNumber == 1000L
        result.getPasDTOvalues.policyNumber == 100000L
        result.getPasDTOvalues.firstName == "John"
        result.getPasDTOvalues.lastname == "Doe"
        result.getPasDTOvalues.age == 25L
        result.getPasDTOvalues.color == "black"
        result.getPasDTOvalues.type == "type"
        result.getPasDTOvalues.effectiveDate == LocalDate.now()
        result.getPasDTOvalues.expirationDate == LocalDate.now()

    }
    def "should be able to savePas() given PasRequestDTO"(){
        when:
        pasService.savePas(PasMockData.generatePasRequestDTO())
        then:
        1 * pasRepository.savePas(_) >> { args ->
            with(args[0] as PasRequestDTO){
                pasDTOChecker(it)
            }
        }
    }
    private void pasDTOChecker(PasRequestDTO it){
        it.values.accountNumber == 1000L
        it.values.policyNumber == 100000L
        it.values.firstName == "John"
        it.values.lastname == "Doe"
        it.values.age == 25L
        it.values.type == "type"
        it.values.color == "black"
        it.values.effectiveDate == LocalDate.now()
        it.values.expirationDate == LocalDate.now()

    }

    @Override
    Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        return null
    }

    @Override
    Specification and(@Nullable Specification other) {
        return super.and(other)
    }

    @Override
    Specification or(@Nullable Specification other) {
        return super.or(other)
    }
}
