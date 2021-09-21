package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Builder;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Value
@Builder
public class ReadAlertError {

  String alertId;

  Exception exception;

  public String toLogMessage() {
    List<String> logElements = new ArrayList<>();
    logElements.add("AlertId: " + alertId);
    logElements.add("Message: " + exception.getMessage());
    logElements.add("StackTrace: ");
    logElements.addAll(getStackTrace());

    return String.join("\r\n", logElements);
  }

  private List<String> getStackTrace() {
    return Arrays
        .stream(exception.getStackTrace())
        .map(StackTraceElement::toString)
        .collect(toList());
  }
}
