package com.smartshop.service;

import com.smartshop.dto.request.PaymentRequestDTO;
import com.smartshop.dto.response.PaymentResponseDTO;

public interface PaymentService {

    public PaymentResponseDTO createPayment(PaymentRequestDTO paymentRequestDTO);
}
