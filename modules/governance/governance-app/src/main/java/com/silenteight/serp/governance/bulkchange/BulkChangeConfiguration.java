package com.silenteight.serp.governance.bulkchange;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@RequiredArgsConstructor
@EnableJpaRepositories
@EntityScan
class BulkChangeConfiguration {

  private final ApplicationEventPublisher applicationEventPublisher;
  private final BulkBranchChangeRepository bulkBranchChangeRepository;

  private final DecisionTreeFacade decisionTreeFacade;
  private final FeatureVectorFinder featureVectorFinder;

  @Bean
  BulkChangeFacade bulkChangeFacade() {
    return new BulkChangeFacade(
        applyBulkBranchChangeHandler(),
        createBulkBranchChangeHandler(),
        rejectBulkBranchChangeHandler(),
        validateBulkChangeUseCase(),
        listBulkBranchChangesUseCase()
    );
  }

  private CreateBulkBranchChangeHandler createBulkBranchChangeHandler() {
    var mapper = new BulkBranchChangeMapper();
    return new CreateBulkBranchChangeHandler(mapper, bulkBranchChangeRepository);
  }

  private RejectBulkBranchChangeHandler rejectBulkBranchChangeHandler() {
    return new RejectBulkBranchChangeHandler(bulkBranchChangeRepository, applicationEventPublisher);
  }

  private ApplyBulkBranchChangeHandler applyBulkBranchChangeHandler() {
    return new ApplyBulkBranchChangeHandler(bulkBranchChangeRepository, applicationEventPublisher);
  }

  private ValidateBulkChangeUseCase validateBulkChangeUseCase() {
    return ValidateBulkChangeUseCase.builder()
        .decisionTreeFacade(decisionTreeFacade)
        .featureVectorFinder(featureVectorFinder)
        .build();
  }

  private ListBulkBranchChangesUseCase listBulkBranchChangesUseCase() {
    return ListBulkBranchChangesUseCase.builder()
        .bulkBranchChangeFinder(bulkBranchChangeFinder())
        .build();
  }

  private BulkBranchChangeFinder bulkBranchChangeFinder() {
    return new BulkBranchChangeFinder(bulkBranchChangeRepository);
  }
}
