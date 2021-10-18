package com.silenteight.warehouse.indexer.query.streaming;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Builder
@Getter
public class ReportFieldDefinitions {

  @NonNull
  List<FieldDefinition> fieldDefinitions;

  public List<String> getNames() {
    return fieldDefinitions.stream()
        .map(FieldDefinition::getName)
        .collect(toList());
  }

  public List<String> getLabels() {
    return fieldDefinitions.stream()
        .map(FieldDefinition::getLabel)
        .collect(toList());
  }
}
