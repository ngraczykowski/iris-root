package com.silenteight.adjudication.engine.alerts.alert.domain;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class RemoveLabelsRequest {

  List<Long> alertIds;

  List<String> labelNames;
}
