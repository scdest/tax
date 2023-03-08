package com.finance.tax.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class TaxCalculationRequest {

    private LocalDateTime invoiceDate;

    private UUID invoiceId;

    private List<TaxRequestLine> taxRequestLine;
}
