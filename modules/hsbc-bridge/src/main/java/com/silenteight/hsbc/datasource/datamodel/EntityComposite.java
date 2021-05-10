package com.silenteight.hsbc.datasource.datamodel;

import java.util.List;

public interface EntityComposite {

  CustomerEntity getCustomerEntity();
  List<WorldCheckEntity> getWorldCheckEntities();
  List<PrivateListEntity> getPrivateListEntities();
  List<CtrpScreening> getCtrpScreeningEntities();
}
