package com.silenteight.payments.bridge.svb.learning.step.store;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.database.JpaItemWriter;

@RequiredArgsConstructor
@Slf4j
class LearningCsvRowWriter<T> extends JpaItemWriter<LearningCsvRowEntity> {

}
