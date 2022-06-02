package com.silenteight.universaldatasource.api.library.comparedates.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.compareDates.v1.CompareDatesFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

@Value
@Builder
public class CompareDatesFeatureInputOut implements Feature {

  String feature;

  String dateToCompare;

  static CompareDatesFeatureInputOut createFrom(CompareDatesFeatureInput input) {
    return CompareDatesFeatureInputOut.builder()
        .feature(input.getFeature())
        .dateToCompare(input.getDateToCompare())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
