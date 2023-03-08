package com.finance.tax;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.tax.dto.DistanceAndPriceByCountry;
import com.finance.tax.dto.TaxCalculationRequest;
import com.finance.tax.dto.TaxRequestLine;
import com.finance.tax.service.TaxCalculationService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TaxCalculationTest {

    @Autowired
    private TaxCalculationService taxCalculationService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void test_Germany_more100km_19tax() throws JsonProcessingException {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(101));
        distanceAndPriceByCountry.setCountryCodeIso3("DEU");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-07-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        System.out.println(objectMapper.writeValueAsString(taxCalculationRequest));

        assertEquals(BigDecimal.valueOf(9.5), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Germany_100kmOrLess_7tax() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(100));
        distanceAndPriceByCountry.setCountryCodeIso3("DEU");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-07-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.valueOf(3.5), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Germany_beforeJuly1st_zeroTax() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(100));
        distanceAndPriceByCountry.setCountryCodeIso3("DEU");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-06-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.ZERO, res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Germany_more50km_20tax_afterAugust1st() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(100));
        distanceAndPriceByCountry.setCountryCodeIso3("DEU");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-09-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.valueOf(10), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Germany_50kmOrLess_7tax_afterAugust1st() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setCountryCodeIso3("DEU");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-09-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.valueOf(3.5), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Austria_100kmOrLess_9tax() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(100));
        distanceAndPriceByCountry.setCountryCodeIso3("AUT");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-07-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.valueOf(4.5), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void test_Austria_more100km_10tax() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(101));
        distanceAndPriceByCountry.setCountryCodeIso3("AUT");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-07-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        assertEquals(BigDecimal.valueOf(5), res.getTaxLines().get(0).getTaxByCountryResponse().get(0).getTaxAmount());

    }

    @Test
    public void testGetResponseById() {

        DistanceAndPriceByCountry distanceAndPriceByCountry = new DistanceAndPriceByCountry();
        distanceAndPriceByCountry.setPrice(BigDecimal.valueOf(50));
        distanceAndPriceByCountry.setDistance(BigDecimal.valueOf(101));
        distanceAndPriceByCountry.setCountryCodeIso3("AUT");

        TaxRequestLine taxRequestLine = new TaxRequestLine();
        taxRequestLine.setId(UUID.randomUUID());
        taxRequestLine.setCountries(List.of(distanceAndPriceByCountry));

        TaxCalculationRequest taxCalculationRequest = new TaxCalculationRequest();
        taxCalculationRequest.setInvoiceDate(LocalDateTime.parse("2029-07-02T00:00:00"));
        taxCalculationRequest.setInvoiceId(UUID.randomUUID());
        taxCalculationRequest.setTaxRequestLine(List.of(taxRequestLine));

        var res = taxCalculationService.calculateTaxes(taxCalculationRequest);

        var resFromDb = taxCalculationService.getResponseById(taxCalculationRequest.getInvoiceId());

        assertEquals(resFromDb, res);

    }
}
