package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.ALWAYS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class SearchAttributes {

  @Nullable
  String title;
  @Nullable
  @JsonInclude(ALWAYS)
  String description;
  @Nullable
  @JsonInclude(ALWAYS)
  Integer hits;
  @Nullable
  @Default
  List<String> columns = List.of();
  @Nullable
  @Default
  @JsonInclude(ALWAYS)
  List<JsonNode> sort = List.of();
  @Nullable
  @Default
  @JsonProperty("kibanaSavedObjectMeta")
  Map<String, String> kibanaSavedObjectMeta = Map.of();
}
