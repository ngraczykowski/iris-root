package com.silenteight.universaldatasource.api.library.historicaldecisions.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v1.HistoricalDecisionsInput.HistoricalDecisionsFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;
import com.silenteight.universaldatasource.api.library.commentinput.v2.StructMapperUtil;

import java.util.Map;

@Value
@Builder
public class HistoricalDecisionsFeatureInputOut implements Feature {

  String feature;
  int truePositiveCount;
  ModelTypeOut modelType;
  Map<String, String> reason;

  static HistoricalDecisionsFeatureInputOut createFrom(HistoricalDecisionsFeatureInput input) {
    return HistoricalDecisionsFeatureInputOut.builder()
        .feature(input.getFeature())
        .truePositiveCount(input.getTruePositiveCount())
        .modelType(ModelTypeOut.createFrom(input.getModelType()))
        .reason(StructMapperUtil.toMap(input.getReason()))
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
