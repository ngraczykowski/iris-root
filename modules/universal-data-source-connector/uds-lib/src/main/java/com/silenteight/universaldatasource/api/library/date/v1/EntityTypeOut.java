package com.silenteight.universaldatasource.api.library.date.v1;

import com.silenteight.datasource.api.date.v1.DateFeatureInput.EntityType;

public enum EntityTypeOut {
  ENTITY_TYPE_UNSPECIFIED, INDIVIDUAL, ORGANIZATION;

  static EntityTypeOut createFrom(EntityType entityType) {
    return EntityTypeOut.valueOf(entityType.name());
  }
}
