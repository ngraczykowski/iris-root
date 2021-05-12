package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

public interface EntityComposite {

  @Nullable
  CustomerEntity getCustomerEntity();
  List<WorldCheckEntity> getWorldCheckEntities();
  List<PrivateListEntity> getPrivateListEntities();
  List<CtrpScreening> getCtrpScreeningEntities();

  default boolean hasPrivateListEntities() {
    return nonNull(getPrivateListEntities()) && !getPrivateListEntities().isEmpty();
  }
}
