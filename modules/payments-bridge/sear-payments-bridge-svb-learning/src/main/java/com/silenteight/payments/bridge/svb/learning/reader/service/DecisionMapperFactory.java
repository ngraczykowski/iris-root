package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.service.DecisionEntry.DecisionKey;

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
    var decisionMap = new HashMap<DecisionKey, DecisionEntry>();
    DecisionEntry decisionEntry = null;

    // NOTE(ahaczewski): Skip the header row.
    csvReader.readNext();

    String[] row;
    while ((row = csvReader.readNext()) != null) {
      var entry = new DecisionEntry(row);

      if (entry.isDefaultCase()) {
        decisionEntry = entry;
      } else {
        decisionMap.put(entry.getDecisionKey(), entry);
      }
    }

    if (decisionEntry == null) {
      throw new IllegalArgumentException("No default value (*,*) in the decision mapper CSV.");
    }

    return new DecisionMapper(decisionMap, decisionEntry);
  }
}
