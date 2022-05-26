package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.resource.csv.file.provider.model.FileRequest;
import com.silenteight.payments.bridge.common.resource.csv.file.provider.port.CsvFileResourceProvider;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serial;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@Service
class SpecificTerms2TermsProvider {

  private static final String COLUMN_NAME = "terms";
  private final CsvFileResourceProvider csvFileResourceProvider;

  public @NotNull List<String> getTermsFromProceededCsvFile(String termsKey, String bucket)
      throws IOException, CsvValidationException {
    var fileRequest = FileRequest.builder().object(termsKey).bucket(bucket).build();

    var inputStreamReader =
        new InputStreamReader(
            csvFileResourceProvider.getResource(fileRequest).getInputStream(),
            Charset.forName("CP1250"));

    List<String> output = genreateCsvFile(inputStreamReader);

    log.info("File {}{} has been processed successfully", bucket, termsKey);

    return output;
  }

  @Nonnull
  private static List<String> genreateCsvFile(InputStreamReader inputStreamReader)
      throws IOException, CsvValidationException {
    var csvReader = new CSVReader(inputStreamReader);

    String header = csvReader.readNext()[0];
    validateCsv(header);

    List<String> output = new ArrayList<>();

    String[] line;
    while ((line = csvReader.readNext()) != null) {
      output.add(line[0]);
    }
    return output;
  }

  private static void validateCsv(@NotNull String header) {
    if (!header.equals(COLUMN_NAME)) {
      throw new CsvTermsValidationException(header);
    }
  }

  private static final class CsvTermsValidationException extends RuntimeException {

    @Serial private static final long serialVersionUID = 3289330233618778067L;

    CsvTermsValidationException(String header) {
      super("CSV column name " + header + " is invalid. Should be " + COLUMN_NAME);
    }
  }
}
