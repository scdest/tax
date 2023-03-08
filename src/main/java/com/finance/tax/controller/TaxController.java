package com.finance.tax.controller;

import com.finance.tax.dto.TaxCalculationRequest;
import com.finance.tax.dto.TaxCalculationResponse;
import com.finance.tax.service.TaxCalculationService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxCalculationService taxCalculationService;

    @PostMapping
    public TaxCalculationResponse calculateTax(@RequestBody TaxCalculationRequest request) {
        return taxCalculationService.calculateTaxes(request);
    }

    @GetMapping("/{invoiceId}")
    public TaxCalculationResponse calculateTax(@PathVariable("invoiceId") String invoiceId) {
        return taxCalculationService.getResponseById(UUID.fromString(invoiceId));
    }
}
