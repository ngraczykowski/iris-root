package com.silenteight.warehouse.report.generation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.time.DateFormatter;
import com.silenteight.warehouse.report.name.ReportFileName;
import com.silenteight.warehouse.report.name.ReportFileNameDto;
import com.silenteight.warehouse.report.persistence.ReportFileExtension;

import java.io.*;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.silenteight.warehouse.common.domain.ReportConstants.IS_PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.PRODUCTION;
import static com.silenteight.warehouse.common.domain.ReportConstants.SIMULATION;
import static com.silenteight.warehouse.report.persistence.ReportFileExtension.*;
import static java.lang.String.valueOf;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.time.ZoneOffset.UTC;

@AllArgsConstructor
@Slf4j
class ReportTempFileCreator {

  @Getter
  private final ReportZipProperties reportZipProperties;
  private final Map<String, ReportFileName> reportNameResolvers;
  private final DateFormatter dateFormatter;

  @SneakyThrows(IOException.class)
  public ParsedFileDto inputStreamToFile(InputStream inputStream, ReportRequestData request) {
    File tempCsvFile = createTempFile(CSV);
    int rowsCount = 0;
    try (
        OutputStream outputStream = Files.newOutputStream(tempCsvFile.toPath(), APPEND);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      while ((line = reader.readLine()) != null) {
        writeReportLine(outputStream, line);
        rowsCount++;
      }
    }
    if (shouldZip(reportZipProperties.getRowsLimit(), rowsCount)) {
      File zippedFile = splitAndZip(tempCsvFile, reportZipProperties.getRowsLimit(), request);
      Files.delete(tempCsvFile.toPath());
      return fileToDto(zippedFile, true);
    }
    return fileToDto(tempCsvFile, false);
  }

  private boolean shouldZip(int rowsLimit, int currentRows) {
    int numberOfRowsWithoutHeader = currentRows - 1;
    return reportZipProperties.isEnabled() && numberOfRowsWithoutHeader > rowsLimit;
  }

  @SneakyThrows(IOException.class)
  private File splitAndZip(File csvFile, int rowsLimit, ReportRequestData request) {
    File destination = createTempFile(ZIP);
    try (
        OutputStream fileOutputStream = Files.newOutputStream(destination.toPath());
        ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);
        BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
      String header = reader.readLine();
      for (int fileNumber = 1; reader.ready(); fileNumber++) {
        zipOutputStream.putNextEntry(
            new ZipEntry(fileNumber + "_" + generateFileName(request)));
        writeSingleZipEntry(zipOutputStream, reader, rowsLimit, header);
        zipOutputStream.closeEntry();
      }
    }
    return destination;
  }

  @SneakyThrows
  private static void writeSingleZipEntry(
      OutputStream outputStream, BufferedReader reader, int rowLimit, String header) {
    writeReportLine(outputStream, header);
    int rowCounter = 1;
    String line;
    while (rowCounter <= rowLimit && (line = reader.readLine()) != null) {
      writeReportLine(outputStream, line);
      rowCounter++;
    }
  }

  @SneakyThrows
  private static void writeReportLine(OutputStream outputStream, String line) {
    outputStream.write(line.getBytes());
    outputStream.write(System.lineSeparator().getBytes());
  }

  @SneakyThrows
  private static File createTempFile(ReportFileExtension extension) {
    return File.createTempFile("temp", "." + extension.getFileExtension());
  }

  private String generateFileName(ReportRequestData request) {
    String type = request.getAnalysisId();
    if (IS_PRODUCTION.test(type)) {
      OffsetDateTime from = request.getFrom().orElse(OffsetDateTime.now());
      OffsetDateTime to = request.getTo().orElse(OffsetDateTime.now());
      return reportNameResolvers.get(PRODUCTION).getReportName(
          ReportFileNameDto.builder()
              .reportType(request.getName())
              .from(dateFormatter.format(from))
              .to(dateFormatter.format(to))
              .timestamp(toTimestamp(request.getCreatedAt()))
              .extension(CSV.getFileExtension())
              .build()
      );
    } else {
      return reportNameResolvers.get(SIMULATION).getReportName(
          ReportFileNameDto.builder()
              .reportType(request.getName())
              .analysisId(type)
              .timestamp(toTimestamp(request.getCreatedAt()))
              .extension(CSV.getFileExtension())
              .build()
      );
    }
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }

  private static ParsedFileDto fileToDto(File reportFile, boolean zipped) {
    return ParsedFileDto.builder()
        .file(reportFile)
        .zipped(zipped)
        .build();
  }
}
