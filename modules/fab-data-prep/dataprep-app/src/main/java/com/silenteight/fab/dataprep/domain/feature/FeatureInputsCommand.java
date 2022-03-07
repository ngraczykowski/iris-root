package com.silenteight.fab.dataprep.domain.feature;

import lombok.Builder;
import lombok.Value;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;

@Value
@Builder
public class FeatureInputsCommand {

  String batchId;
  ExtractedAlert extractedAlert;
}
