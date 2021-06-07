package com.silenteight.hsbc.bridge.model.transfer;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface ModelRepository extends Repository<ModelEntity, Long> {

  void save(ModelEntity modelEntity);
}
