package com.silenteight.universaldatasource.api.library.document.v1;

import lombok.Builder;
import lombok.Value;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.universaldatasource.api.library.Feature;
import com.silenteight.universaldatasource.api.library.FeatureBuilderProvider;

import java.util.List;

@Value
@Builder
public class DocumentFeatureInputOut implements Feature {

  String feature;

  @Builder.Default
  List<String> alertedPartyDocuments = List.of();

  @Builder.Default
  List<String> watchlistDocuments = List.of();

  static DocumentFeatureInputOut createFrom(DocumentFeatureInput input) {
    return DocumentFeatureInputOut.builder()
        .feature(input.getFeature())
        .alertedPartyDocuments(input.getAlertedPartyDocumentsList())
        .watchlistDocuments(input.getWatchlistDocumentsList())
        .build();
  }

  @Override
  public void accept(FeatureBuilderProvider provider, FeatureInput.Builder builder) {
    provider.build(this, builder);
  }
}
