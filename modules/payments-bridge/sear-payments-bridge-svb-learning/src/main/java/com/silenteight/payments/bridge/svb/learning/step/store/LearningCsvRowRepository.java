
package com.silenteight.payments.bridge.svb.learning.step.store;

import org.springframework.data.repository.Repository;

import java.util.List;


public interface LearningCsvRowRepository extends Repository<LearningCsvRowEntity, Long> {

  LearningCsvRowEntity save(LearningCsvRowEntity entity);

  List<LearningCsvRowEntity> findAll();

}
