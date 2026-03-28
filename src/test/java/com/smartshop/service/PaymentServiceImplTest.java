package com.smartshop.service;

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
import com.smartshop.service.impl.PaymentServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private CommandeRepository commandeRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Commande commande;
    private Payment payment;
    private PaymentResponseDTO paymentResponseDTO;

    @BeforeEach
    void setUp() {
        commande = Commande.builder()
                .id(1L).totalTTC(1000.0).montantRestant(1000.0)
                .statutCommande(StatutCommande.PENDING).numeroPaiement(0)
                .payments(new ArrayList<>()).build();

        payment = Payment.builder()
                .id(1L).montant(500.0).reference("REF-001").banque("BMCE")
                .typePayment(TypePayment.VIREMENT).statutPayment(StatutPayment.EN_ATTENTE)
                .numeroPaiement(1).commande(commande).build();

        paymentResponseDTO = PaymentResponseDTO.builder()
                .id(1L).montant(500.0).reference("REF-001").build();
    }

    @Test
    void createPayment_WithValidEspecesPayment_ShouldSucceed() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L).montant(500.0).reference("RECU-001")
                .typePayment(TypePayment.ESPECE).build();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));
        when(paymentMapper.toEntity(dto)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toResponseDto(payment)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentService.createPayment(dto);

        assertNotNull(result);
        assertEquals(500.0, result.getMontant());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_WithEspecesOver20000_ShouldThrowException() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L).montant(25000.0).reference("RECU-001")
                .typePayment(TypePayment.ESPECE).build();

        assertThrows(PaymentNotPossibleException.class,
                () -> paymentService.createPayment(dto));
    }

    @Test
    void createPayment_WithNonPendingCommande_ShouldThrowException() {
        commande.setStatutCommande(StatutCommande.CONFIRMED);
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L).montant(500.0).reference("REF-001")
                .banque("BMCE").typePayment(TypePayment.VIREMENT).build();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        assertThrows(PaymentNotPossibleException.class,
                () -> paymentService.createPayment(dto));
    }


    @Test
    void createPayment_WithAmountGreaterThanRemaining_ShouldThrowException() {
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L).montant(1500.0).reference("REF-001")
                .banque("BMCE").typePayment(TypePayment.VIREMENT).build();

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        assertThrows(PaymentNotPossibleException.class,
                () -> paymentService.createPayment(dto));
    }

    @Test
    void updatePaymentToEncaisse_WithValidPayment_ShouldSucceed() {
        payment.setStatutPayment(StatutPayment.EN_ATTENTE);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(commandeRepository.save(any(Commande.class))).thenReturn(commande);
        when(paymentMapper.toResponseDto(payment)).thenReturn(paymentResponseDTO);

        PaymentResponseDTO result = paymentService.updatePaymentToEncaisse(1L);

        assertNotNull(result);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
    @Test
    void createPaymentCheque_WithoutBankName_ShouldThrowException(){
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L)
                .montant(500.0)
                .reference("CHQ-12345")
                .typePayment(TypePayment.CHEQUE)
                .banque(null)
                .build();

        assertThrows(ValidationException.class,
                () -> paymentService.createPayment(dto));


    }

    @Test
    void createPayment_WithNonValidId_ThrowsException(){
        PaymentRequestDTO dto = PaymentRequestDTO.builder()
                .commandeId(1L)
                .montant(500.0)
                .reference("CHQ-12345")
                .typePayment(TypePayment.CHEQUE)
                .banque("BMCI")
                .build();
        when(commandeRepository.findById(1L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class,
                () -> paymentService.createPayment(dto));

    }

}
