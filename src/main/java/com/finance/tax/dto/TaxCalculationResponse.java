package com.finance.tax.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class TaxCalculationResponse {

    UUID invoiceId;

    List<TaxLineResponse> taxLines;
}
