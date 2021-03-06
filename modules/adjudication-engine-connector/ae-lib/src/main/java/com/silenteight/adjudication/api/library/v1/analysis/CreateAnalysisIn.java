package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.Builder;
import lombok.Value;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@Builder
@Value
public class CreateAnalysisIn {

  String name;
  String policy;
  String strategy;
  List<String> categories;
  List<FeatureIn> features;
  @Nullable NotificationFlagsIn notificationFlags;
}
