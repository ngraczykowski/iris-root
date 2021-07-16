package com.silenteight.serp.governance.qa.manage.domain;

import org.springframework.data.repository.Repository;

interface AlertRepository extends Repository<Alert, Long> {

  Alert findById(Long id);

  Alert findByDiscriminator(String discriminator);

  Alert save(Alert alert);
}
