package com.silenteight.fab.dataprep.domain;

import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.Optional;

interface AlertRepository extends Repository<AlertEntity, Long> {

  Collection<AlertEntity> findAll();

  AlertEntity save(AlertEntity alertEntity);

  Optional<AlertEntity> findByDiscriminator(String discriminator);
}
