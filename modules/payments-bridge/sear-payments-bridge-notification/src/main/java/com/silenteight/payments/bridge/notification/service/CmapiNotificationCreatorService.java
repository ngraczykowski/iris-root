package com.silenteight.payments.bridge.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.notification.model.CmapiNotificationRequest;
import com.silenteight.payments.bridge.notification.model.Notification;
import com.silenteight.payments.bridge.notification.port.CmapiNotificationCreatorUseCase;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.exception.ExceptionUtils.rethrow;

@Slf4j
@RequiredArgsConstructor
@Service
class CmapiNotificationCreatorService implements CmapiNotificationCreatorUseCase {

  private static final String[] HEADERS =
      { "Alert Id", "Alert Name", "Message Id", "System Id", "Message" };

  private static final String CMAPI_PROCESSING_ERROR = "CMAPI_PROCESSING_ERROR";
  private static final String TEMP_FILE_NAME = "CMAPI_ERRORS";
  private static final String CSV_EXTENSION = ".csv";

  @Override
  public Notification createCmapiNotification(CmapiNotificationRequest cmapiNotificationRequest) {
    return Notification.builder()
        .type(CMAPI_PROCESSING_ERROR)
        .message(createMessage())
        .attachment(createAttachment(cmapiNotificationRequest))
        .attachmentName(TEMP_FILE_NAME + CSV_EXTENSION)
        .build();
  }

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

      for (var file : files) {
        processCsvFile(data, file);
      }

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

  private static String createMessage() {
    return "<html>\n"
        + "<body>\n"
        + "<p>This is to let you know there was an error with alert’s processing.</p>\n"
        + "<p>Please see the details in the file attached.</p>\n"
        + "<br>\n"
        + "<p>Thank you.</p>\n"
        + "<p>Silent Eight</p>\n"
        + "</body>\n"
        + "</html>\n";
  }

  private static byte[] createAttachment(CmapiNotificationRequest cmapiNotificationRequest) {

    File csvAttachment = createCsvFile(cmapiNotificationRequest);
    try {
      return Files.readAllBytes(csvAttachment.toPath());
    } catch (IOException exception) {
      log.error(
          "Error during attachment creation. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    } finally {
      deleteFile(csvAttachment);
    }
  }

  private static File createCsvFile(CmapiNotificationRequest cnr) {
    File file = createTempFile(cnr.getAlertId(), CSV_EXTENSION);

    try (
        FileWriter outputfile = new FileWriter(file);
        CSVWriter writer = new CSVWriter(outputfile)) {
      List<String[]> data = new ArrayList<>();
      data.add(HEADERS);
      data.add(new String[] {
          cnr.getAlertId(), cnr.getAlertName(), cnr.getMessageId(), cnr.getSystemId(),
          cnr.getMessage() });
      writer.writeAll(data);
      return file;
    } catch (IOException exception) {
      log.error(
          "Error during creating csv file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }
  }

  private static File createZipFile(File file) throws IOException {

    File zipFile = createTempFile("zipFile", null);

    try (
        ZipOutputStream zipFileOutput = new ZipOutputStream(new FileOutputStream(zipFile));
        InputStream input = new FileInputStream(file)) {

      zipFileOutput.putNextEntry(new ZipEntry(file.getName()));
      int temp = 0;
      while ((temp = input.read()) != -1) {
        zipFileOutput.write(temp);
      }

    } catch (IOException exception) {
      log.error(
          "Error during creating zip file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }

    return zipFile;
  }

  private static File createTempFile(String name, @Nullable String extension) {
    try {
      return File.createTempFile(name, extension);
    } catch (IOException exception) {
      log.error(
          "Error during creating temp file. Message= {}, reason= {}.",
          exception.getMessage(), exception.getCause());
      return rethrow(exception);
    }
  }

  private static void deleteFile(@Nullable File file) {
    Optional.ofNullable(file).ifPresent(f -> {
      try {
        Files.deleteIfExists(f.toPath());
      } catch (IOException exception) {
        log.error(
            "Error during deleting temp file, path={}. Message= {}, reason= {}.",
            f.toPath(), exception.getMessage(), exception.getCause());
      }
    });
  }
}
