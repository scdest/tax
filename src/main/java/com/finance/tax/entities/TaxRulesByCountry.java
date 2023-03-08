package com.finance.tax.entities;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaxRulesByCountry {

    private String countryCodeIso3;

    private LocalDateTime dateValid;

    private List<TaxRuleByDistance> taxRuleByDistance;
}
