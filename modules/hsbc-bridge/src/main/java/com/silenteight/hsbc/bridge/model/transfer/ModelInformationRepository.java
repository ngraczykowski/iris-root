package com.silenteight.hsbc.bridge.model.transfer;

import org.springframework.data.repository.Repository;

@org.springframework.stereotype.Repository
interface ModelInformationRepository extends Repository<ModelInformationEntity, Long> {

  void save(ModelInformationEntity modelInformationEntity);
}
