package com.silenteight.payments.bridge.svb.learning.reader.service;

import org.springframework.data.repository.Repository;

interface LearningErrorRepository extends Repository<LearningErrorEntity, Long> {

  void save(LearningErrorEntity error);

  void saveAll(Iterable<LearningErrorEntity> errors);

}
