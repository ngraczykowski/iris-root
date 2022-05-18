package com.silenteight.payments.bridge.agents.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.agents.service.HistoricalRiskAssessmentAgent.ConfigTuple;

import com.opencsv.CSVReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
class HistoricalRiskAssessmentBlackListCsvReader {

  private static final String COL_NAME_ONE = "Name / Account Number";
  private static final String COL_NAME_TWO = "FKCO_V_LIST_FMM_ID";

  static List<ConfigTuple> getConfigListFromCsv(
      ResourceLoader resourceLoader, String blackListCsvUrl) throws IOException {
    Resource resource = resourceLoader.getResource(blackListCsvUrl);
    try (var reader = new InputStreamReader(resource.getInputStream())) {
      return fromCsv(reader);
    }
  }

  private static List<ConfigTuple> fromCsv(Reader reader) {
    try {
      var csvReader = new CSVReader(reader);
      String[] header = csvReader.readNext();
      validateCsv(header);

      List<ConfigTuple> output = new ArrayList<>();

      String[] line;
      while ((line = csvReader.readNext()) != null) {
        output.add(lineToConfigTuple(line));
      }
      return output;
    } catch (Exception e) {
      return rethrow(e);
    }
  }

  private static ConfigTuple lineToConfigTuple(String[] line) {
    return new ConfigTuple(line[0], line[1]);
  }

  private static void validateCsv(String[] header) {
    if (!isFormValid(header)) {
      throw new IllegalArgumentException(
          String.format(
              "CSV column names are invalid. Should be %s, %s",
              COL_NAME_ONE, COL_NAME_TWO));
    }
  }

  private static boolean isFormValid(String[] header) {
    return Arrays.equals(header, new String[] {
        COL_NAME_ONE, COL_NAME_TWO });
  }
}
