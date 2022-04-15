package com.silenteight.serp.governance.qa.manage.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface AlertRepository extends Repository<Alert, Long> {

  Optional<Alert> findById(Long id);

  Optional<Alert> findByAlertName(String alertName);

  @Query(value = "SELECT a.id FROM Alert a WHERE a.alertName IN (:alertNames)")
  List<Long> findIdByAlertNameIn(List<String> alertNames);

  Alert save(Alert alert);

  void delete(Alert alert);
}
