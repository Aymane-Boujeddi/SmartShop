package com.smartshop.service.impl;

import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.entity.Commande;
import com.smartshop.entity.Payment;
import com.smartshop.enums.StatutCommande;
import com.smartshop.enums.StatutPayment;
import com.smartshop.enums.TypePayment;
import com.smartshop.exception.PaymentNotPossibleException;
import com.smartshop.exception.ValidationException;
import com.smartshop.mapper.PaymentMapper;
import com.smartshop.repository.CommandeRepository;
import com.smartshop.repository.PaymentRepository;
import com.smartshop.service.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final CommandeRepository commandeRepository;

    private final PaymentMapper paymentMapper;


    @Transactional
    @Override
    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO) {

        validatePaymentFields(paymentRequestDTO);
        if(paymentRequestDTO.getTypePayment().equals(TypePayment.ESPECE) && paymentRequestDTO.getMontant() > 20000.0){
            throw new PaymentNotPossibleException("Cannot pay this amount in with the type ESPECE ");
        }

        Commande commande = getCommandeByID(paymentRequestDTO.getCommandeId());
        commandeEligibleForPayment(commande, paymentRequestDTO.getMontant());
        Payment payment = paymentMapper.toEntity(paymentRequestDTO);
        StatutPayment statutPayment = getStatutFromPaymentType(paymentRequestDTO.getTypePayment());

        if(statutPayment.equals(StatutPayment.ENCAISSE)){
            payment.setDateEncaissement(LocalDateTime.now());
            commande.setMontantRestant(commande.getMontantRestant() - paymentRequestDTO.getMontant());
        }

        payment.setCommande(commande);
        payment.setNumeroPaiement(commande.getNumeroPaiement() + 1);
        payment.setStatutPayment(statutPayment);
        payment.setMontant(paymentRequestDTO.getMontant());

        Payment savedPayment = paymentRepository.save(payment);
        commande.setNumeroPaiement(savedPayment.getNumeroPaiement());

        commandeRepository.save(commande);
        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    public List<PaymentResponseDTO> getPaymentsForCommande(Long id) {
        List<Payment> paymentList = getCommandeByID(id).getPayments();
        if(paymentList.isEmpty()){
            throw new EntityNotFoundException("There is not payments for this Commande with id : " + id);
        }
        return paymentList.stream().map(paymentMapper::toResponseDto).toList();
    }

    @Override
    public List<PaymentResponseDTO> getAllPayments() {

        return paymentRepository.findAll().stream().map(paymentMapper::toResponseDto).toList();
    }

    @Override
    public PaymentResponseDTO getPaymentById(Long id) {

        return paymentMapper.toResponseDto(findPaymentById(id));
    }

    @Transactional
    @Override
    public PaymentResponseDTO updatePaymentToEncaisse(Long id) {
        Payment payment = findPaymentById(id);
        Commande commande = payment.getCommande();
        if(payment.getStatutPayment().equals(StatutPayment.ENCAISSE)){
            throw new PaymentNotPossibleException("Payment is already updated to ENCAISSE" );
        } else if (payment.getStatutPayment().equals(StatutPayment.REJETE)) {
            throw new PaymentNotPossibleException("Payment is Rejected cannot update it to  ENCAISSE");

        }
        payment.setStatutPayment(StatutPayment.ENCAISSE);
        payment.setDateEncaissement(LocalDateTime.now());
        commande.setMontantRestant(commande.getMontantRestant() - payment.getMontant());

        Commande savedCommande = commandeRepository.save(commande);
        payment.setCommande(savedCommande);
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(savedPayment);
    }

    @Override
    public PaymentResponseDTO updatePaymentToRejete(Long id) {
        Payment payment = findPaymentById(id);
        if(payment.getStatutPayment().equals(StatutPayment.ENCAISSE)){
            throw new PaymentNotPossibleException("Payment Confirmed cannot be Rejected  ");
        } else if (payment.getStatutPayment().equals(StatutPayment.REJETE)) {
            throw new PaymentNotPossibleException("Payment already Rejected  ");
        }
        payment.setStatutPayment(StatutPayment.REJETE);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponseDto(savedPayment);
    }

    private Payment findPaymentById(Long id){
        return paymentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Payment not found with this id : "+ id));
    }
    private StatutPayment getStatutFromPaymentType(TypePayment typePayment){
        if(typePayment.equals(TypePayment.ESPECE)){
            return StatutPayment.ENCAISSE;
        }else{
            return StatutPayment.EN_ATTENTE;
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
    private void validatePaymentFields(PaymentRequestDTO dto) {
        TypePayment type = dto.getTypePayment();
        List<String> errors = new ArrayList<>();

        if (type == TypePayment.ESPECE && (dto.getReference() == null || dto.getReference().isBlank())) {
            errors.add("Reference (reçu) is required for ESPÈCES payment");
        }

        if ((type == TypePayment.CHEQUE || type == TypePayment.VIREMENT) && (dto.getBanque() == null || dto.getBanque().isBlank())) {
            errors.add("Banque is required for " + type + " payment");
        }

        if ((type == TypePayment.CHEQUE || type == TypePayment.VIREMENT) && (dto.getReference() == null || dto.getReference().isBlank())) {
            errors.add("Reference is required for " + type + " payment");
        }

        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }
    }

}
