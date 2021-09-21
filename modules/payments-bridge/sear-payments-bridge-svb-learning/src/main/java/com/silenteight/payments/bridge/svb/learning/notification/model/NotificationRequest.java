package com.silenteight.payments.bridge.svb.learning.notification.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import java.util.List;

@Value
@Builder
public class NotificationRequest {

  String fileName;

  long fileLength;

  String hash;

  int successfullyRecords;

  int failedRecord;

  List<ReadAlertError> errorLogs;

  public String toBodyHtml() {
    return String.format("<!DOCTYPE html>\n"
        + "<html>\n"
        + "<body>\n"
        + "\n"
        + "<p>File name: %s</p>\n"
        + "<p>File length: %d</p>\n"
        + "<p>File hash: %s</p>\n"
        + "<p>Successful Records count: %d</p>\n"
        + "<p>Failed Records count: %d</p>\n"
        + "\n"
        + "</body>\n"
        + "</html>\n", fileName, fileLength, hash, successfullyRecords, failedRecord);
  }

  public String toBodyText() {
    return String.format(
          "File name: %s \r\n"
            + "File length: %d \r\n"
            + "File hash: %s< \r\n"
            + "Successful Records count: %d \r\n"
            + "Failed Records count: %d \r\n",
        fileName, fileLength, hash, successfullyRecords, failedRecord);
  }
}
