package com.silenteight.payments.bridge.firco.datasource.model;

import lombok.Builder;
import lombok.Value;

import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

@Value
@Builder
public class CategoryValueExtractModel {

  HitData hitData;

  String alertName;

  String matchName;
}
