package com.silenteight.payments.bridge.common.model;

import lombok.Value;

import java.util.List;

@Value
public class RegisteredAlert {

  String systemId;
  String messageId;
  String alertName;
  List<String> matches;

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }
}
