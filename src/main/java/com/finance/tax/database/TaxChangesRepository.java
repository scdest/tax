package com.finance.tax.database;

import com.finance.tax.dto.TaxCalculationResponse;
import com.finance.tax.entities.TaxRuleByDistance;
import com.finance.tax.entities.TaxRulesByCountry;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TaxChangesRepository {

    private static final String GERMANY_CODE = "DEU";

    private static final String AUSTRIA_CODE = "AUT";

    private static final ConcurrentHashMap<UUID, TaxCalculationResponse> responseById = new ConcurrentHashMap<>();

    private static final List<TaxRulesByCountry> taxChanges = List.of(
        TaxRulesByCountry.builder()
            .dateValid(LocalDateTime.parse("2029-07-01T00:00:00.00"))
            .taxRuleByDistance(
                List.of(
                    buildRule(BigDecimal.valueOf(100), BigDecimal.valueOf(7)),
                    buildRule(BigDecimal.valueOf(Integer.MAX_VALUE), BigDecimal.valueOf(19))
                )
            )
            .countryCodeIso3(GERMANY_CODE)
            .build(),
        TaxRulesByCountry.builder()
            .dateValid(LocalDateTime.parse("2029-08-01T00:00:00.00"))
            .taxRuleByDistance(
                List.of(
                    buildRule(BigDecimal.valueOf(50), BigDecimal.valueOf(7)),
                    buildRule(BigDecimal.valueOf(Integer.MAX_VALUE), BigDecimal.valueOf(20))
                )
            )
            .countryCodeIso3(GERMANY_CODE)
            .build(),
        TaxRulesByCountry.builder()
            .dateValid(LocalDateTime.parse("2029-07-01T00:00:00.00"))
            .taxRuleByDistance(
                List.of(
                    buildRule(BigDecimal.valueOf(100), BigDecimal.valueOf(9)),
                    buildRule(BigDecimal.valueOf(Integer.MAX_VALUE), BigDecimal.valueOf(10))
                )
            )
            .countryCodeIso3(AUSTRIA_CODE)
            .build()
    );

    private static TaxRuleByDistance buildRule(BigDecimal maxDistance, BigDecimal percent) {
        return TaxRuleByDistance.builder()
            .maxDistance(maxDistance)
            .taxPercent(percent)
            .build();
    }

    public void saveResponse(TaxCalculationResponse response) {
        responseById.put(response.getInvoiceId(), response);
    }

    public TaxCalculationResponse getResponse(UUID id) {
        return responseById.get(id);
    }

    public Optional<TaxRulesByCountry> getTaxRulesByCountry(String countryIso3Code, LocalDateTime current) {
        return taxChanges.stream().filter(
            taxChange -> taxChange.getCountryCodeIso3().equals(countryIso3Code) &&
                taxChange.getDateValid().isBefore(current)
        ).max(Comparator.comparing(TaxRulesByCountry::getDateValid));
    }
}
