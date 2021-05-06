package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@Data
public class SavedObjectResponsePaginatedList {

  private Integer page;
  private Integer perPage;
  private Integer total;
  @JsonProperty("saved_objects")
  private List<SavedObject> savedObjects;
}
