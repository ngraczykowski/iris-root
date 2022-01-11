package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.Value;

import java.util.List;

@Value
public class RegisteredAlert {

  String messageId;
  String systemId;
  String alertName;
  List<RegisteredMatch> matches;

  public String getDiscriminator() {
    return systemId + "|" + messageId;
  }
}
