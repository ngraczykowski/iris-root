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
      String alertDiscriminator, List<String> matchDiscriminators) {
    String elasticWriteIndexName = productionMatchNamingStrategy.getElasticWriteIndexName();

    matchDiscriminators.forEach(matchDiscriminator ->
        productionMatchRepository.updateIfNotExists(
            alertDiscriminator, matchDiscriminator, elasticWriteIndexName));

    return productionMatchRepository.findAllByDiscriminatorIn(matchDiscriminators).stream()
        .collect(toMap(
            ProductionMatchEntity::getDiscriminator,
            ProductionMatchEntity::getIndexName));
  }
}
