package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@Service
class SpecificTerms2AwsFileProvider {

  private static final String COLUMN_NAME = "terms";

  public @NotNull List<String> proceedCsvFile(String termsKey, String bucket, String region) {
    log.info("Sending request to S3");

    try (
        var s3Client = S3Client.builder().region(Region.of(region)).build();
        InputStreamReader inputStreamReader = getInputStreamReaderFromAws(
            termsKey, bucket, s3Client)) {

      List<String> output = genreateCsvFile(inputStreamReader);

      log.info("File {}{} has been processed successfully", bucket, termsKey);

      return output;

    } catch (S3Exception | CsvValidationException | IOException e) {
      log.error(
          "There was a problem when receiving or proceeding s3 object - Message: {}, Reason: {}",
          e.getMessage(),
          e.getCause());
      throw new AwsS3Exception(e);
    }
  }

  @Nonnull
  private List<String> genreateCsvFile(InputStreamReader inputStreamReader) throws IOException,
      CsvValidationException {
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

  @Nonnull
  private InputStreamReader getInputStreamReaderFromAws(
      String termsKey, String bucket, S3Client s3Client) {
    log.info("Received S3 CSV object");
    var responseInputStream = s3Client.getObject(
        GetObjectRequest
            .builder()
            .bucket(bucket)
            .key(termsKey)
            .build());
    log.info("Object get from s3");
    return new InputStreamReader(responseInputStream, Charset.forName("CP1250"));
  }

  private void validateCsv(@NotNull String header) {
    if (!header.equals(COLUMN_NAME)) {
      throw new CsvTermsValidationException(header);
    }
  }

  private static final class AwsS3Exception extends RuntimeException {

    private static final long serialVersionUID = 3289330223618728867L;

    AwsS3Exception(Exception e) {
      super("There was a problem when receiving s3 object = " + e.getMessage());
    }
  }

  private static final class CsvTermsValidationException extends RuntimeException {

    private static final long serialVersionUID = 3289330233618778067L;

    CsvTermsValidationException(String header) {
      super("CSV column name " + header + " is invalid. Should be " + COLUMN_NAME);
    }
  }
}
