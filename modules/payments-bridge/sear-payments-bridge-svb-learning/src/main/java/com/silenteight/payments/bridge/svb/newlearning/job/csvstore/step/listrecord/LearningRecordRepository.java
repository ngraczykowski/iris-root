package com.silenteight.payments.bridge.svb.newlearning.job.csvstore.step.listrecord;

import org.springframework.data.repository.CrudRepository;

public interface LearningRecordRepository extends CrudRepository<LearningListedRecordEntity, Long> {
}
