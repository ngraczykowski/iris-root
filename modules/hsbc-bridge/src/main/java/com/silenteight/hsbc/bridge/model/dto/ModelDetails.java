package com.silenteight.hsbc.bridge.model.dto;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.model.transfer.ModelManager.Details;

@Value
@Builder
public class ModelDetails implements Details {

  String type;
  String version;
}
