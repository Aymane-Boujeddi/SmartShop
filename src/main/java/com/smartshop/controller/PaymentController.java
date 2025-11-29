package com.smartshop.controller;


import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;
import com.smartshop.service.PaymentService;
import com.smartshop.util.SecurityUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    @PostMapping("/payment")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(paymentService.createPayment(paymentRequestDTO));
    }

    @GetMapping("/payment/commande/{id}")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsForOneCommande(@PathVariable Long id, HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(paymentService.getPaymentsForCommande(id));
    }

    @GetMapping("/payment")
    public ResponseEntity<List<PaymentResponseDTO>> getAllPayments(HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentResponseDTO> getPaymentById(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @PutMapping("/payment-encaisse/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentToEncaisse(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok(paymentService.updatePaymentToEncaisse(id));
    }

    @PutMapping("/payment-rejete/{id}")
    public ResponseEntity<PaymentResponseDTO> updatePaymentToRejecte(@PathVariable Long id,HttpSession session){
        SecurityUtil.checkAdmin(session);
        return ResponseEntity.ok();
    }
}
