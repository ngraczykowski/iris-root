package com.silenteight.serp.governance.qa.manage.domain;

import org.springframework.data.repository.Repository;

interface AlertRepository extends Repository<Alert, Long> {

  Alert findById(Long id);

  Alert findByAlertName(String alertName);

  Alert save(Alert alert);
}
