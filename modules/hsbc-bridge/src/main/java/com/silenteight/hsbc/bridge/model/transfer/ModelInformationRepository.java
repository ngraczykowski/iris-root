package com.silenteight.hsbc.bridge.model.transfer;

import org.springframework.data.repository.Repository;

import java.util.Optional;

@org.springframework.stereotype.Repository
interface ModelInformationRepository extends Repository<ModelInformationEntity, Long> {

  void save(ModelInformationEntity modelInformationEntity);

  Optional<ModelInformationEntity> findFirstByTypeOrderByCreatedAtDesc(ModelType modelType);
}
