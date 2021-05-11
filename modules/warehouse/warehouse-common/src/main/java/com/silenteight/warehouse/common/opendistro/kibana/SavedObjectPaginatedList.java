package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import javax.annotation.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
class SavedObjectPaginatedList<T> {

  @Nullable
  private Integer page;
  @Nullable
  @JsonProperty("per_page")
  private Integer perPage;
  @Nullable
  private Integer total;
  @Nullable
  @JsonProperty("saved_objects")
  private List<SavedObject<T>> savedObjects;
}
