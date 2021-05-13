package com.silenteight.warehouse.indexer.analysis;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = AnalysisTestConfiguration.class)
class AnalysisServiceTest extends BaseDataJpaTest {

  private static final String ANALYSIS_ID = "07fdee7c-9a66-416d-b835-32aebcb63013";
  private static final String ANALYSIS = "analysis/" + ANALYSIS_ID;

  @Autowired
  AnalysisService analysisService;

  @Autowired
  TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @AfterEach
  public void cleanup() {
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
  }

  @Test
  void shouldStoreDevelopmentSimulationAnalysis() {
    analysisService.getAnalysisMetadata(ANALYSIS, new SimulationNamingStrategy("env1"));

    List<AnalysisMetadataEntity> allEntries = testAnalysisMetadataRepository.findAll();
    assertThat(allEntries).hasSize(1);
    AnalysisMetadataEntity analysisMetadataEntity = allEntries.get(0);
    assertThat(analysisMetadataEntity.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getElasticIndexPattern()).isEqualTo(
        "env1_simulation_" + ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getTenant()).isEqualTo("env1_simulation_" + ANALYSIS_ID);
  }

  @Test
  void shouldStoreProductionSimulationAnalysis() {
    analysisService.getAnalysisMetadata(ANALYSIS, new ProductionNamingStrategy("env2"));

    List<AnalysisMetadataEntity> allEntries = testAnalysisMetadataRepository.findAll();
    assertThat(allEntries).hasSize(1);
    AnalysisMetadataEntity analysisMetadataEntity = allEntries.get(0);
    assertThat(analysisMetadataEntity.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getElasticIndexPattern()).isEqualTo("env2_production");
    assertThat(analysisMetadataEntity.getTenant()).isEqualTo("env2_production");
  }
}
