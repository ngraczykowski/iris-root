package com.silenteight.hsbc.bridge.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.model.transfer.ModelManager.Details;

@Value
@Builder
public class ModelDetails implements Details {

  String type;
  String name;
}
