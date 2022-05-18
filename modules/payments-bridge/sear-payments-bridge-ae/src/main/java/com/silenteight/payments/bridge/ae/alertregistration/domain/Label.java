package com.silenteight.payments.bridge.ae.alertregistration.domain;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(staticName = "of")
public class Label {

  String name;

  String value;
}
