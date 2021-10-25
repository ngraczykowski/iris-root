package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.Files.newOutputStream;

class GenerateLogFileUseCase {

  static File generateLogFile(List<ReadAlertError> errorLogs) {
    var fileName = generateFileName();
    try {
      FileWriter writer = new FileWriter(fileName);
      for (var err : errorLogs) {
        writer.write(err.toLogMessage());
      }
      writer.close();
      fileName = zipFile(fileName);
    } catch (IOException e) {
      throw new GeneratingFileException(e);
    }
    return new File(fileName);
  }

  private static String zipFile(String sourceFile) throws IOException {
    String zipFileName = sourceFile.substring(0, sourceFile.length() - 4) + ".zip";
    var fos = newOutputStream(Path.of(zipFileName));
    var zipOut = new ZipOutputStream(fos);
    var fileToZip = new File(sourceFile);
    var fis = new FileInputStream(fileToZip);
    var zipEntry = new ZipEntry(fileToZip.getName());
    zipOut.putNextEntry(zipEntry);
    byte[] bytes = new byte[1024];
    int length;
    while ((length = fis.read(bytes)) >= 0) {
      zipOut.write(bytes, 0, length);
    }
    zipOut.close();
    fis.close();
    fos.close();
    return zipFileName;
  }

  private static String generateFileName() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
    LocalDateTime now = LocalDateTime.now();
    return "/tmp/error-logs-" + dtf.format(now) + ".log";
  }

  private static class GeneratingFileException extends RuntimeException {

    private static final long serialVersionUID = -190974158224092115L;

    GeneratingFileException(Exception e) {
      super(e);
    }
  }
}
