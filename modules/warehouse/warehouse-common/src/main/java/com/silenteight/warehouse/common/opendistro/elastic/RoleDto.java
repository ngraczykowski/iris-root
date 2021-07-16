package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

  @JsonProperty("index_permissions")
  @Default
  List<IndexPermission> indexPermissions = new LinkedList<>();

  @JsonIgnore
  public long getIndexPermissionCount() {
    return indexPermissions.size();
  }

  @JsonIgnore
  public String getFirstIndexPermissionDls() {
    return getIndexPermissions().get(0).getDls();
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class IndexPermission {

    @JsonProperty("index_patterns")
    @Default
    List<String> indexPatterns = new LinkedList<>();

    String dls;
  }
}
