package com.silenteight.serp.governance.qa.manage.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

interface AlertRepository extends Repository<Alert, Long> {

  Optional<Alert> findById(Long id);

  Optional<Alert> findByDiscriminator(String discriminator);

  @Query(value = "SELECT a.id FROM Alert a WHERE a.discriminator IN (:discriminators)")
  List<Long> findIdByDiscriminatorIn(List<String> discriminators);

  Alert save(Alert alert);

  void delete(Alert alert);
}
