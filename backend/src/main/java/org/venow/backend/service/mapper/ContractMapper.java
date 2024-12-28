package org.venow.backend.service.mapper;

import org.venow.backend.domain.model.Contract;
import org.venow.backend.domain.model.Price;
import org.venow.backend.domain.model.Status;
import org.venow.backend.service.dto.ContractDTO;
import org.venow.backend.service.dto.PriceDto;

import java.math.BigDecimal;

public class ContractMapper {

    public static Contract toEntity(ContractDTO contractDTO) {
        return Contract.builder()
                .id(contractDTO.getId())
                .contractId(contractDTO.getContractId())
                .organizer(UserMapper.mapToEntity(contractDTO.getOrganizer()))
                .exhibitor(UserMapper.mapToEntity(contractDTO.getExhibitor()))
                .application(ApplicationMapper.mapToEntity(contractDTO.getApplication()))
                .terms(contractDTO.getTerms())
                .price(toPrice(contractDTO.getPrice()))
                .boothNumber(contractDTO.getBoothNumber())
                .status(Status.valueOf(contractDTO.getStatus()))
                .build();
    }

    private static Price toPrice(PriceDto priceDto) {
        if (priceDto == null) {
            return null;
        }
        return Price.builder()
                .amountInCents(priceDto.getAmountInEuro().multiply(BigDecimal.valueOf(100)).intValue())
                .build();
    }

    public static ContractDTO toDTO(Contract contract) {
        return ContractDTO.builder()
                .id(contract.getId())
                .contractId(contract.getContractId())
                .organizer(UserMapper.mapToDTO(contract.getOrganizer()))
                .exhibitor(UserMapper.mapToDTO(contract.getExhibitor()))
                .application(ApplicationMapper.mapToDTO(contract.getApplication()))
                .terms(contract.getTerms())
                .organizer(UserMapper.mapToDTO(contract.getOrganizer()))
                .exhibitor(UserMapper.mapToDTO(contract.getExhibitor()))
                .price(toPriceDto(contract.getPrice()))
                .boothNumber(contract.getBoothNumber())
                .status(contract.getStatus().name())
                .build();
    }

    private static PriceDto toPriceDto(Price price) {
        if (price == null) {
            return null;
        }
        return new PriceDto(price.getAmountInCents());
    }
}
