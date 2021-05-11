package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class SavedObjectReference {

  @Nullable
  private String name;
  @Nullable
  private String type;
  @Nullable
  private String id;

  public boolean hasType(SavedObjectType otherType) {
    return otherType.getId().equals(type);
  }

  public void substituteReference(Map<String, String> idMapping) {
    String oldId = getId();
    String newId = idMapping.get(oldId);
    if (nonNull(oldId) && nonNull(newId))
      setId(newId);
  }
}
