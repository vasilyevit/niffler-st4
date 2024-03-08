package guru.qa.niffler.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record TestData(
    @JsonIgnore String password,
    @JsonIgnore List<CategoryJson> categoryJsons,
    @JsonIgnore List<SpendJson> spendJsons
) {
}
