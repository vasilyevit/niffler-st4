package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nonnull;

public record CurrencyJson(
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("currencyRate")
        Double currencyRate) {

}
