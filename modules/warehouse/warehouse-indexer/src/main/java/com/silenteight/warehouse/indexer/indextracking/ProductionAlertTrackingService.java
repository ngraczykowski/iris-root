package com.silenteight.warehouse.indexer.indextracking;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class ProductionAlertTrackingService {

  @NotNull
  private final ProductionAlertRepository productionAlertRepository;

  @NotNull
  private final ProductionNamingStrategy productionNamingStrategy;

  @Transactional
  public Map<String, String> getIndexNameByDiscriminator(List<String> discriminators) {
    String elasticWriteIndexName = productionNamingStrategy.getElasticWriteIndexName();

    discriminators.forEach(discriminator ->
        productionAlertRepository.updateIfNotExists(discriminator, elasticWriteIndexName));

    return productionAlertRepository.findAllByDiscriminatorIn(discriminators).stream()
        .collect(toMap(
            ProductionAlertEntity::getDiscriminator,
            ProductionAlertEntity::getIndexName));
  }
}
