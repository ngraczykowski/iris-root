package com.silenteight.serp.governance.migrate;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.serp.governance.activation.ActivationService;
import com.silenteight.serp.governance.branch.BranchService;
import com.silenteight.serp.governance.common.signature.SignatureCalculator;
import com.silenteight.serp.governance.decisiongroup.DecisionGroupFinder;
import com.silenteight.serp.governance.decisiontree.DecisionTreeFacade;
import com.silenteight.serp.governance.featureset.FeatureSetFinder;
import com.silenteight.serp.governance.featurevector.FeatureVectorFinder;
import com.silenteight.serp.governance.featurevector.FeatureVectorService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MigrationModuleConfiguration {

  @Bean
  ImportService importService(
      AuditingLogger auditingLogger,
      DecisionTreeFacade decisionTreeService,
      FeatureVectorService featureVectorService,
      DecisionGroupFinder decisionGroupFinder,
      ActivationService activationService,
      BranchService branchService,
      SignatureCalculator signatureCalculator) {
    return ImportService.builder()
        .auditingLogger(auditingLogger)
        .decisionTreeService(decisionTreeService)
        .featureVectorService(featureVectorService)
        .decisionGroupFinder(decisionGroupFinder)
        .activationService(activationService)
        .branchService(branchService)
        .calculator(signatureCalculator)
        .build();
  }

  @Bean
  ExportService exportService(
      FeatureSetFinder featureSetFinder, FeatureVectorFinder featureVectorFinder) {
    return new ExportService(featureSetFinder, featureVectorFinder, DefaultTimeSource.INSTANCE);
  }

}
