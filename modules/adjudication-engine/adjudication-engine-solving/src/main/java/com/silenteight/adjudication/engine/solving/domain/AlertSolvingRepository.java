package com.silenteight.adjudication.engine.solving.domain;

public interface AlertSolvingRepository {

  AlertSolving get(final Long key);

  void save(final AlertSolving model);
}
