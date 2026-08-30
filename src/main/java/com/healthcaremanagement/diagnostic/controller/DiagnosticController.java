package com.healthcaremanagement.diagnostic.controller;

import com.healthcaremanagement.common.response.ApiResponse;
import com.healthcaremanagement.diagnostic.dto.CheckoutRequest;
import com.healthcaremanagement.diagnostic.dto.OrderResponse;
import com.healthcaremanagement.diagnostic.service.DiagnosticService;
import com.healthcaremanagement.security.CurrentUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/diagnostic")  // Base path for all diagnostic endpoints
@RequiredArgsConstructor
public class DiagnosticController {

    private final DiagnosticService diagnosticService;

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderResponse>> checkout(
            @CurrentUser String userId,
            @Valid @RequestBody CheckoutRequest request) {
        OrderResponse order = diagnosticService.createOrder(userId, request);
        // Initiate payment (maybe via Stripe/PayPal) – return order with payment link
        return ResponseEntity.ok(ApiResponse.success(order));
    }
}