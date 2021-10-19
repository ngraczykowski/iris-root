package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.annotation.Nonnull;

@RequiredArgsConstructor
@Component
class DecisionMapperFactory {

  public DecisionMapper create(InputStream inputStream) throws IOException, CsvValidationException {
    try (var csvReader = new CSVReader(new InputStreamReader(inputStream))) {
      return doCreate(csvReader);
    }
  }

  @Nonnull
  private DecisionMapper doCreate(CSVReader csvReader) throws IOException, CsvValidationException {
    var decisionMap = new HashMap<String, String>();
    String defaultValue = null;

    // NOTE(ahaczewski): Skip the header row.
    csvReader.readNext();

    String[] row;
    while ((row = csvReader.readNext()) != null) {
      if ("*".equals(row[0])) {
        defaultValue = row[1];
      } else {
        decisionMap.put(row[0], row[1]);
      }
    }

    if (defaultValue == null) {
      throw new IllegalArgumentException("No default value (*) in the decision mapper CSV.");
    }

    return new DecisionMapper(decisionMap, defaultValue);
  }
}
