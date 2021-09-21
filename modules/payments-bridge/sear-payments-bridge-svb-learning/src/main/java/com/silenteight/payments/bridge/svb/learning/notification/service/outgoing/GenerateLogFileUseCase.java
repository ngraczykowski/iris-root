package com.silenteight.payments.bridge.svb.learning.notification.service.outgoing;

import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

class GenerateLogFileUseCase {

  static File generateLogFile(List<ReadAlertError> errorLogs) {
    var fileName = generateFileName();
    try {
      FileWriter writer = new FileWriter(fileName);
      for (var err : errorLogs) {
        writer.write(err.toLogMessage());
      }
      writer.close();
    } catch (IOException e) {
      throw new GeneratingFileException(e);
    }
    return new File(fileName);
  }

  private static String generateFileName() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH:mm");
    LocalDateTime now = LocalDateTime.now();
    return "error-logs-" + dtf.format(now) + ".log";
  }

  private static class GeneratingFileException extends RuntimeException {

    private static final long serialVersionUID = -190974158224092115L;

    GeneratingFileException(Exception e) {
      super(e);
    }
  }
}
