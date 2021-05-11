package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SearchAttributes {

  @Nullable
  String title;
  @Nullable
  String description;
  @Nullable
  Integer hits;
  @Nullable
  @Default
  List<String> columns = List.of();
  @Nullable
  @Default
  List<String> sort = List.of();
  @Nullable
  @Default
  @JsonProperty("kibanaSavedObjectMeta")
  Map<String, String> kibanaSavedObjectMeta = Map.of();
}
