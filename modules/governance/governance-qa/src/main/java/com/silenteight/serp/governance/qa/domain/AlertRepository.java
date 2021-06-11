package com.silenteight.serp.governance.qa.domain;

import org.springframework.data.repository.Repository;

public interface AlertRepository extends Repository<Alert, Long> {

  Alert findById(Long id);

  Alert findByAlertName(String alertName);

  Alert save(Alert alert);
}
