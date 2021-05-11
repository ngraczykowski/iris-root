package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;
import javax.annotation.Nullable;

public interface EntityComposite {

  @Nullable
  CustomerEntity getCustomerEntity();
  List<WorldCheckEntity> getWorldCheckEntities();
  List<PrivateListEntity> getPrivateListEntities();
  List<CtrpScreening> getCtrpScreeningEntities();
}
