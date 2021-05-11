package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.NonFinal;

import java.util.List;

@Value
@AllArgsConstructor
@Builder(access = AccessLevel.PACKAGE)
public class KibanaIndexPatternDto {

  @Setter
  @NonFinal
  @NonNull
  String id;
  @Getter(AccessLevel.PACKAGE)
  @NonNull
  KibanaIndexPatternAttributes attributes;
  @Getter(AccessLevel.PACKAGE)
  @NonNull
  @Default
  List<SavedObjectReference> references = List.of();

  public void setElasticIndexName(String elasticIndexName) {
    attributes.setTitle(elasticIndexName);
  }

  public String getElasticIndexName() {
    return attributes.getTitle();
  }
}
