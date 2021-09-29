package com.silenteight.payments.bridge.firco.decision.statemapping;

import lombok.RequiredArgsConstructor;

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
@Component
class CsvPatternTupleLoader {

  private static final String[] COLUMNS =
      { "DataCenter", "SourceState", "Unit", "Recommendation", "DestinationState" };

  List<PatternTuple> load(Reader reader) {
    try {
      var csvReader = new CSVReader(reader);
      String[] header = csvReader.readNext();
      validateCsv(header);

      List<PatternTuple> patternTuples = new ArrayList<>();
      String[] row;

      var rowNumber = 1L;
      while ((row = csvReader.readNext()) != null) {
        validateRow(rowNumber++, row);

        var patternTuple = makePatternTuple(row);
        patternTuples.add(patternTuple);
      }

      return patternTuples;
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

  private static PatternTuple makePatternTuple(String[] patternArray) {
    String dataCenter = patternArray[0];
    String sourceState = patternArray[1];
    String unit = patternArray[2];
    String recommendation = patternArray[3];
    String destinationState = patternArray[4];

    return new PatternTuple(
        dataCenter,
        sourceState,
        unit,
        recommendation,
        destinationState);
  }
}
