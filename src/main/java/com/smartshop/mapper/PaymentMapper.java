package com.smartshop.mapper;

import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    @Mapping(target = "commandeId",source = "commande.id")
    PaymentResponseDTO toResponseDto(Payment payment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "numeroPaiement", ignore = true)
    @Mapping(target = "montant", ignore = true)
    @Mapping(target = "statutPayment", ignore = true)
    @Mapping(target = "datePayment", ignore = true)
    @Mapping(target = "dateEncaissement", ignore = true)
    @Mapping(target = "commande", ignore = true)
    Payment toEntity(PaymentRequestDTO paymentRequestDTO);
}
