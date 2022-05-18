package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.port.NotificationAttachmentUseCase;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static com.silenteight.payments.bridge.common.app.FileProcessor.createTempFile;
import static com.silenteight.payments.bridge.common.app.FileProcessor.createZipFile;
import static com.silenteight.payments.bridge.common.app.FileProcessor.deleteFile;
import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
@RequiredArgsConstructor
@Service
class NotificationAttachmentService implements NotificationAttachmentUseCase {

  private static final String[] HEADERS =
      { "Alert Id", "Alert Name", "Message Id", "System Id", "Message" };
  private static final String TEMP_FILE_NAME = "CMAPI_ERRORS";
  private static final String CSV_EXTENSION = ".csv";

  @Override
  public byte[] mergeCsvAttachments(List<byte[]> files) {
    File zippedAttachment = null;
    File newCsvFile = extractMergedCsvFile(files);
    try {
      zippedAttachment = createZipFile(newCsvFile);
      return Files.readAllBytes(zippedAttachment.toPath());
    } catch (IOException exception) {
      log.error(
          "Error during merging attachments. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    } finally {
      deleteFile(newCsvFile);
      deleteFile(zippedAttachment);
    }
  }

  private static File extractMergedCsvFile(List<byte[]> files) {
    File newCsvFile = createTempFile(TEMP_FILE_NAME, CSV_EXTENSION);
    try (
        FileWriter outputFile = new FileWriter(newCsvFile);
        CSVWriter writer = new CSVWriter(outputFile);) {

      List<String[]> data = new ArrayList<>();
      data.add(HEADERS);

      files.forEach(file -> processCsvFile(data, file));

      writer.writeAll(data);
      return newCsvFile;
    } catch (IOException exception) {
      log.error(
          "Error during reading csv files. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }
  }

  private static void processCsvFile(List<String[]> data, byte[] file) {
    try (CSVReader csvReader = new CSVReader(new StringReader(new String(file)))) {
      csvReader.skip(1);
      String[] csvRecord = csvReader.readNext();
      data.add(csvRecord);
    } catch (IOException | CsvValidationException exception) {
      log.error(
          "Error during reading csv files. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
    }
  }
}
