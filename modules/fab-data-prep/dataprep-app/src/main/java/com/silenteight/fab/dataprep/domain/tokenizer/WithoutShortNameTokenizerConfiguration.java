package com.silenteight.fab.dataprep.domain.tokenizer;

import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.ColumnType;

import java.util.List;

class WithoutShortNameTokenizerConfiguration implements TokenizerConfiguration {

  private static final List<String> COLUMNS = List.of(
      "salutation",
      "name",
      "customerType",
      "dob",
      "dateOfEstablishment",
      "gender",
      "swiftBic",
      "address1",
      "address2",
      "city",
      "state",
      "country",
      "countryOfIncorporation",
      "countryOfDomicile",
      "countryOfBirth",
      "customerSegment",
      "profession",
      "passportNum",
      "nationalId",
      "tradeLicPlaceOfIssue",
      "groupOrCompanyName",
      "source",
      "sourceSystemId",
      "customerNumber",
      "alternate",
      "latestCustomerNumber",
      "lastUpdateTime"
  );

  private static final CsvSchema CSV_SCHEMA = CsvSchema.builder()
      .setColumnSeparator(SEPARATOR)
      .addColumns(COLUMNS, ColumnType.STRING)
      .build();

  @Override
  public CsvSchema getConfiguration() {
    return CSV_SCHEMA;
  }
}
