package com.finance.tax.dto;

import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class TaxLineResponse {

    UUID id;

    List<TaxByCountryResponse> taxByCountryResponse;
}
