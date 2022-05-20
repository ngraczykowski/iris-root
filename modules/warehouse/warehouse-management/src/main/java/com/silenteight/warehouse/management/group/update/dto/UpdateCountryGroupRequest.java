package com.silenteight.warehouse.management.group.update.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCountryGroupRequest {

  @NonNull
  private String name;
}
