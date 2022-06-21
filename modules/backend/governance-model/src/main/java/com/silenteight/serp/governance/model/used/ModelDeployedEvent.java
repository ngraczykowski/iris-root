package com.silenteight.serp.governance.model.used;

import lombok.Value;

@Value(staticConstructor = "of")
public class ModelDeployedEvent {

  String model;
}
