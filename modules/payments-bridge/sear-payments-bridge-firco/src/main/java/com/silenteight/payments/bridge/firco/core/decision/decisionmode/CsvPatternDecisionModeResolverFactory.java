package com.silenteight.payments.bridge.firco.core.decision.decisionmode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@RequiredArgsConstructor
@Slf4j
@Component
class CsvPatternDecisionModeResolverFactory {

  private static final String[] COLUMNS = { "Pattern", "DecisionMode" };

  PatternDecisionModeResolver create(Reader reader) {
    try {
      var csvReader = new CSVReader(reader);
      String[] header = csvReader.readNext();
      validateCsv(header);

      DecisionMode defaultDecisionMode = null;
      List<UnitPatternDecisionTuple> tuples = new ArrayList<>();
      String[] row;

      var rowNumber = 1L;
      while ((row = csvReader.readNext()) != null) {
        validateRow(rowNumber++, row);

        var decisionTuple = makeUnitPatternDecisionTuple(row);

        tuples.add(decisionTuple);

        if ("*".equals(decisionTuple.getUnitPattern().trim())) {
          defaultDecisionMode = decisionTuple.getMode();
        }
      }

      if (defaultDecisionMode == null) {
        throw new IllegalArgumentException("There was no default pattern '*' (only asterisk). "
            + "Default pattern is mandatory to match all possible units.");
      }

      if (log.isDebugEnabled())
        log.debug("About to create PatternDecisionModeResolver."
            + " defaultDecisionMode: {}, tuples: {}, ", defaultDecisionMode, tuples);

      return new PatternDecisionModeResolver(tuples, defaultDecisionMode);
    } catch (CsvValidationException | IOException e) {
      return rethrow(e);
    }
  }

  private static void validateCsv(String[] header) {
    if (!isFormValid(header)) {
      throw new IllegalArgumentException(
          "CSV column names are invalid. Should be " + String.join(", ", COLUMNS));
    }
  }

  private static boolean isFormValid(String[] header) {
    return Arrays.equals(header, COLUMNS);
  }

  private static void validateRow(long rowNumber, String[] row) {
    if (row.length != COLUMNS.length) {
      throw new IllegalArgumentException(
          "CSV row " + rowNumber + " is invalid. Expected " + COLUMNS.length + " values, got "
              + row.length);
    }
  }

  private static UnitPatternDecisionTuple makeUnitPatternDecisionTuple(String[] row) {
    String pattern = row[0];
    String decisionMode = row[1];

    return new UnitPatternDecisionTuple(pattern, DecisionMode.valueOf(decisionMode));
  }
}
