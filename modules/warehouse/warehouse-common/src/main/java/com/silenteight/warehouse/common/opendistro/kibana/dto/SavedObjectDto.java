package com.silenteight.warehouse.common.opendistro.kibana.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.util.HashMap;
import java.util.Map;

@Value
@Builder
public class SavedObjectDto {

  @NonNull
  String id;
  @NonNull
  SavedObjectAttributes attributes;

  public static class SavedObjectAttributes extends HashMap<String, String> {

    private static final long serialVersionUID = -1021618665392802641L;
    private static final String ELASTIC_INDEX_PATTERN = "title";

    public SavedObjectAttributes(Map<String, String> attributes) {
      super(attributes);
    }

    public void setElasticIndex(String elasticIndexPattern) {
      put(ELASTIC_INDEX_PATTERN, elasticIndexPattern);
    }
  }
}
