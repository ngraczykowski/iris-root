package com.silenteight.hsbc.bridge.model.transfer;

import com.silenteight.hsbc.bridge.model.dto.ModelStatus;
import com.silenteight.hsbc.bridge.model.dto.ModelType;

import org.springframework.data.repository.Repository;

import java.util.Optional;

@org.springframework.stereotype.Repository
interface ModelInformationRepository extends Repository<ModelInformationEntity, Long> {

  void save(ModelInformationEntity modelInformationEntity);

  Optional<ModelInformationEntity> findFirstByTypeAndStatusOrderByCreatedAtDesc(
      ModelType modelType, ModelStatus status);
}
