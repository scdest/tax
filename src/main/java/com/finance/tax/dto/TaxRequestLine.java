package com.finance.tax.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class TaxRequestLine {

    private UUID id;
    private List<DistanceAndPriceByCountry> countries;
}
