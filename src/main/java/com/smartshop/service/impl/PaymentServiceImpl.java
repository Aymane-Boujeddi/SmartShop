package com.smartshop.service.impl;

import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Commande;
import com.smartshop.entity.Payment;
import com.smartshop.enums.StatutCommande;
import com.smartshop.enums.StatutPayment;
import com.smartshop.enums.TypePayment;
import com.smartshop.exception.PaymentNotPossibleException;
import com.smartshop.mapper.PaymentMapper;
import com.smartshop.repository.CommandeRepository;
import com.smartshop.repository.PaymentRepository;
import com.smartshop.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CommandeRepository commandeRepository;

    private final PaymentMapper paymentMapper;


    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {
        Commande commande = getCommandeByID(paymentRequestDTO.getCommandeId());
        commandeEligibleForPayment(commande, paymentRequestDTO.getMontant());
        Payment payment = paymentMapper.toEntity(paymentRequestDTO);
        StatutPayment statutPayment = getStatutFromPaymentType(paymentRequestDTO.getTypePayment());
        int numeroPayment = commande.getNumeroPaiement() + 1;

        if(statutPayment.equals(StatutPayment.ENCAISSE)){
            payment.setDateEncaissement(LocalDateTime.now());
        }
        payment.setCommande(commande);
        payment.setNumeroPaiement(numeroPayment);
        payment.setStatutPayment(statutPayment);
        payment.setMontant(paymentRequestDTO.getMontant());

        Payment savedPayment = paymentRepository.save(payment);




        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsForCommande(Long id) {
        List<Payment> paymentList = getCommandeByID(id).getPayments();
        return paymentList.stream().map(paymentMapper::toResponseDto).toList();
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll().stream().map(paymentMapper::toResponseDto).toList();
    }

    private StatutPayment getStatutFromPaymentType(TypePayment typePayment){
        if(typePayment.equals(TypePayment.CHEQUE)){
            return StatutPayment.EN_ATTENTE;
        }else{
            return StatutPayment.ENCAISSE;
        }
    }
    private Commande getCommandeByID(Long id){
        return commandeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Commande not found with this id : "+ id));
    }
    private void commandeEligibleForPayment(Commande commande, Double montant){
        if(!commande.getStatutCommande().equals(StatutCommande.PENDING)){
            throw new PaymentNotPossibleException("Payment can't be added for Commande with status : " +
                    commande.getStatutCommande());
        }

        if(commande.getMontantRestant() == 0){
            throw new PaymentNotPossibleException("Payment can't be added . This Commande is fully paid");
        }
        if(montant > commande.getMontantRestant()){
            throw new PaymentNotPossibleException("Payment can't be added . Montant : " + montant +
                    " , is greater than Commande Montant restant : " + commande.getMontantRestant());
        }

    }

}
