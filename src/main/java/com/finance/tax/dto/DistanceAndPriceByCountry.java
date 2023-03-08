package com.finance.tax.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DistanceAndPriceByCountry {

    private String countryCodeIso3;

    private BigDecimal distance;

    private BigDecimal price;

}
