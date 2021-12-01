package com.silenteight.warehouse.indexer.production.indextracking;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class ProductionAlertTrackingService {

  @NonNull
  private final ProductionAlertRepository productionAlertRepository;

  @NonNull
  private final ProductionAlertNamingStrategy productionAlertNamingStrategy;

  @Transactional
  public Map<String, String> getIndexNameByDiscriminator(List<String> discriminators) {
    String elasticWriteIndexName = productionAlertNamingStrategy.getElasticWriteIndexName();

    discriminators.forEach(discriminator ->
        productionAlertRepository.updateIfNotExists(discriminator, elasticWriteIndexName));

    return productionAlertRepository.findAllByDiscriminatorIn(discriminators).stream()
        .collect(toMap(
            ProductionAlertEntity::getDiscriminator,
            ProductionAlertEntity::getIndexName));
  }
}
