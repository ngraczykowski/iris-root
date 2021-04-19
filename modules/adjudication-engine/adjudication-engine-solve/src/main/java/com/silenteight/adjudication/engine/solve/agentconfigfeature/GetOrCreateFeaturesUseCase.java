package com.silenteight.adjudication.engine.solve.agentconfigfeature;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
class GetOrCreateFeaturesUseCase {

  @Transactional
  void getOrCreateFeatures() {

    // INSERT ... ON CONFLICT DO NOTHING
    // SELECT ... FROM

    // https://stackoverflow.com/questions/34708509/how-to-use-returning-with-on-conflict-in-postgresql/42217872#42217872
    // https://stackoverflow.com/questions/56453690/hibernate-thread-safe-idempotent-upsert-without-constraint-exception-handling
    // https://dba.stackexchange.com/questions/212580/concurrent-transactions-result-in-race-condition-with-unique-constraint-on-inser
    // https://stackoverflow.com/questions/6722344/select-or-insert-a-row-in-one-command
  }
}
