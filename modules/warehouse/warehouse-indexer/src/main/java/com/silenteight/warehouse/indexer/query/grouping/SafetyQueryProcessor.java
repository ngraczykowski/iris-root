package com.silenteight.warehouse.indexer.query.grouping;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.query.index.FieldsQueryIndexService;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@RequiredArgsConstructor
public class SafetyQueryProcessor {

  @NonNull
  private final List<String> allAvailableFields;

  SafetyQueryProcessor(
      @NonNull List<String> indexes,
      @NonNull FieldsQueryIndexService queryIndexService) {

    allAvailableFields = indexes.stream()
        .flatMap(index -> queryIndexService.getFieldsList(index).stream())
        .collect(toList());
  }

  List<String> filterOutNonExistingFields(List<String> fields) {
    List<String> filteredFields = new ArrayList<>(fields);
    filteredFields.retainAll(allAvailableFields);

    if (fields.size() != filteredFields.size())
      log.warn("Some fields are missing: Requested fields={}, Effective fields={}",
          fields, filteredFields);

    return filteredFields;
  }
}
