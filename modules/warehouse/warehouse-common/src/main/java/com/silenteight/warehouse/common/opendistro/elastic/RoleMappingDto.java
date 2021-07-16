package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleMappingDto {

  @JsonProperty("backend_roles")
  @Default
  Set<String> backendRoles = new HashSet<>();
}
