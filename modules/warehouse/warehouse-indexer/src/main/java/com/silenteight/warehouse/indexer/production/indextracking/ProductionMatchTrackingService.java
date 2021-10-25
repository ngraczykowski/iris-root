package com.silenteight.warehouse.indexer.production.indextracking;

import lombok.RequiredArgsConstructor;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;

import static java.util.stream.Collectors.toMap;

@RequiredArgsConstructor
public class ProductionMatchTrackingService {

  @NotNull
  private final ProductionMatchRepository productionMatchRepository;

  @NotNull
  private final ProductionMatchNamingStrategy productionMatchNamingStrategy;

  @Transactional
  public Map<String, String> getIndexNameByDiscriminator(
      String alertDiscriminator, List<String> discriminators) {
    String elasticWriteIndexName = productionMatchNamingStrategy.getElasticWriteIndexName();

    discriminators.forEach(discriminator ->
        productionMatchRepository.updateIfNotExists(
            alertDiscriminator, discriminator, elasticWriteIndexName));

    return productionMatchRepository.findAllByDiscriminatorIn(discriminators).stream()
        .collect(toMap(
            ProductionMatchEntity::getDiscriminator,
            ProductionMatchEntity::getIndexName));
  }
}
