package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AlertMetaData;
import com.silenteight.payments.bridge.svb.learning.reader.domain.ReadAlertError;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class LearningErrorService {

  private final LearningErrorRepository learningErrorRepository;

  void save(LearningAlertBatch batch) {
    if (!batch.getErrors().isEmpty()) {
      return;
    }
    learningErrorRepository.saveAll(
        mapToEntities(batch.getAlertMetaData(), batch.getErrors()));
  }

  private List<LearningErrorEntity> mapToEntities(
      AlertMetaData alertMetaData, List<ReadAlertError> errors) {
    return errors.stream().map(error -> {
      var entity = new LearningErrorEntity(error);
      entity.setFileName(alertMetaData.getFileName());
      entity.setBatchStamp(alertMetaData.getBatchStamp());
      return entity;
    }).collect(Collectors.toList());
  }

}
