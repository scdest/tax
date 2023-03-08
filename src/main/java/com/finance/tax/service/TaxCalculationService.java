package com.finance.tax.service;

import com.finance.tax.database.TaxChangesRepository;
import com.finance.tax.dto.DistanceAndPriceByCountry;
import com.finance.tax.dto.TaxByCountryResponse;
import com.finance.tax.dto.TaxCalculationRequest;
import com.finance.tax.dto.TaxCalculationResponse;
import com.finance.tax.dto.TaxLineResponse;
import com.finance.tax.dto.TaxRequestLine;
import com.finance.tax.entities.TaxRuleByDistance;
import com.finance.tax.entities.TaxRulesByCountry;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaxCalculationService {

    private final TaxChangesRepository repository;

    public TaxCalculationResponse calculateTaxes(TaxCalculationRequest request) {
        List<TaxLineResponse> taxLineResponses = new ArrayList<>();

        for (TaxRequestLine taxRequestLine: request.getTaxRequestLine()) {
            TaxLineResponse taxLineResponse = new TaxLineResponse();
            taxLineResponse.setId(taxRequestLine.getId());
            taxLineResponse.setTaxByCountryResponse(new ArrayList<>());

            for (DistanceAndPriceByCountry distance : taxRequestLine.getCountries()) {
                BigDecimal percent = getTaxPercent(
                    distance.getCountryCodeIso3(),
                    request.getInvoiceDate(),
                    distance.getDistance()
                );
                TaxByCountryResponse taxByCountryResponse = new TaxByCountryResponse();
                taxByCountryResponse.setCountryCode(distance.getCountryCodeIso3());
                taxByCountryResponse.setTaxAmount(
                    distance.getPrice().multiply(percent).divide(BigDecimal.valueOf(100))
                );
                taxLineResponse.getTaxByCountryResponse().add(taxByCountryResponse);
            }
            taxLineResponses.add(taxLineResponse);
        }

        TaxCalculationResponse taxByCountryResponse = new TaxCalculationResponse();
        taxByCountryResponse.setTaxLines(taxLineResponses);
        taxByCountryResponse.setInvoiceId(request.getInvoiceId());
        repository.saveResponse(taxByCountryResponse);
        return taxByCountryResponse;
    }

    public TaxCalculationResponse getResponseById(UUID id) {
        return repository.getResponse(id);
    }

    private BigDecimal getTaxPercent(String countryCode, LocalDateTime current, BigDecimal distance) {
        Optional<TaxRulesByCountry> taxRulesByCountry = repository.getTaxRulesByCountry(countryCode, current);
        if (taxRulesByCountry.isEmpty()) {
            return BigDecimal.ZERO;
        }

        Optional<TaxRuleByDistance> taxRule = taxRulesByCountry.get().getTaxRuleByDistance()
            .stream().filter(taxRuleByDistance -> taxRuleByDistance.getMaxDistance().compareTo(distance) >= 0)
            .min(Comparator.comparing(TaxRuleByDistance::getMaxDistance));

        if (taxRule.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return taxRule.get().getTaxPercent();
    }
}
