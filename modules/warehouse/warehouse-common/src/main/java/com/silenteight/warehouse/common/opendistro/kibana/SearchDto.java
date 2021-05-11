package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.*;
import lombok.Builder.Default;
import lombok.experimental.NonFinal;

import java.util.List;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class SearchDto {

  @NonFinal
  @NonNull
  @Setter
  String id;
  @NonNull
  @Getter(AccessLevel.PACKAGE)
  SearchAttributes attributes;
  @NonNull
  @Default
  @Getter(AccessLevel.PACKAGE)
  List<SavedObjectReference> references = List.of();

  public void substituteReferences(
      SavedObjectType referenceType, Map<String, String> kibanaIndexMapping) {

    references.stream()
        .filter(reference -> reference.hasType(referenceType))
        .forEach(reference -> reference.substituteReference(kibanaIndexMapping));
  }
}

