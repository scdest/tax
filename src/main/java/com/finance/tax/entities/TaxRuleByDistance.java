package com.finance.tax.entities;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxRuleByDistance {

    private BigDecimal maxDistance;

    private BigDecimal taxPercent;
}
