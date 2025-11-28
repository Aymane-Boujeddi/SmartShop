package com.smartshop.service;

import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;

import java.util.List;

public interface PaymentService {

    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
    public List<PaymentResponseDTO> getPaymentsForCommande(Long id);
    public List<PaymentResponseDTO> getAllPayments();
}
