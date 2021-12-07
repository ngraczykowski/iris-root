
package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.store;

import org.springframework.data.repository.Repository;

import java.util.List;


public interface LearningCsvRowRepository extends Repository<LearningCsvRowEntity, Long> {

  LearningCsvRowEntity save(LearningCsvRowEntity entity);

  List<LearningCsvRowEntity> findAll();

}
