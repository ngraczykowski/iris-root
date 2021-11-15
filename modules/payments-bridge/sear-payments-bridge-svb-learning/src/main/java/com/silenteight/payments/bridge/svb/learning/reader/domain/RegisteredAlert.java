package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class RegisteredAlert {

  UUID alertId;
  String systemId;
  String messageId;
  String alertName;
  List<String> matches;

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }
}
