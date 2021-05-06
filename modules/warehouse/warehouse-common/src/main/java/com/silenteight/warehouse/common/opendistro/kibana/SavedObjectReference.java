package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Data;

import javax.annotation.Nullable;

@Data
public class SavedObjectReference {

  @Nullable
  private String name;
  @Nullable
  private String type;
  @Nullable
  private String id;
}
