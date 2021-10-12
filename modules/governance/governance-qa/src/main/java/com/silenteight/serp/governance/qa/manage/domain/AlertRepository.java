package com.silenteight.serp.governance.qa.manage.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface AlertRepository extends Repository<Alert, Long> {

  Alert findById(Long id);

  Optional<Alert> findByDiscriminator(String discriminator);

  Alert save(Alert alert);

  void delete(Alert alert);
}
