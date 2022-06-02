package com.silenteight.universaldatasource.api.library.isofgivendocumenttype.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.isofgivendocumenttype.v1.IsOfGivenDocumentTypeFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class IsOfGivenDocumentTypeFeatureInputOut implements Feature {

  String feature;

  String documentNumber;

  @Builder.Default
  List<String> documentTypes = List.of();

  static IsOfGivenDocumentTypeFeatureInputOut createFrom(IsOfGivenDocumentTypeFeatureInput input) {
    return IsOfGivenDocumentTypeFeatureInputOut.builder()
        .feature(input.getFeature())
        .documentNumber(input.getDocumentNumber())
        .documentTypes(input.getDocumentTypesList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
