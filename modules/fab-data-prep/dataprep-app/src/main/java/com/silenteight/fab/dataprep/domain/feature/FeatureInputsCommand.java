package com.silenteight.fab.dataprep.domain.feature;

import lombok.Builder;
import lombok.Value;

import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;

@Value
@Builder
public class FeatureInputsCommand {

  String batchId;
  RegisteredAlert registeredAlert;
}
