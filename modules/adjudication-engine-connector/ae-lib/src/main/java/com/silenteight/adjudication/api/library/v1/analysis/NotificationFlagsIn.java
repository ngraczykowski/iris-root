package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class NotificationFlagsIn {

  boolean attachRecommendation;
  boolean attachMetadata;
}
