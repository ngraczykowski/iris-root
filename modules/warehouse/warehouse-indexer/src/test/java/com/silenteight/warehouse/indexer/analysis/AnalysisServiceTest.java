package com.silenteight.warehouse.indexer.analysis;

import com.silenteight.sep.base.testing.BaseDataJpaTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.indexer.analysis.AnalysisMetadataFixture.*;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("jpa-test")
@ContextConfiguration(classes = AnalysisTestConfiguration.class)
class AnalysisServiceTest extends BaseDataJpaTest {

  @Autowired
  AnalysisService underTest;

  @Autowired
  TestAnalysisMetadataRepository testAnalysisMetadataRepository;

  @Autowired
  AnalysisMetadataRepository analysisMetadataRepository;

  @AfterEach
  public void cleanup() {
    testAnalysisMetadataRepository.truncateAnalysisMetadata();
  }

  @Test
  void shouldStoreDevelopmentSimulationAnalysis() {
    underTest.getOrCreateAnalysisMetadata(ANALYSIS, new SimulationNamingStrategy("env1"));

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
    underTest.getOrCreateAnalysisMetadata(ANALYSIS, new ProductionNamingStrategy("env2"));

    List<AnalysisMetadataEntity> allEntries = testAnalysisMetadataRepository.findAll();
    assertThat(allEntries).hasSize(1);
    AnalysisMetadataEntity analysisMetadataEntity = allEntries.get(0);
    assertThat(analysisMetadataEntity.getAnalysisId()).isEqualTo(ANALYSIS_ID);
    assertThat(analysisMetadataEntity.getElasticIndexPattern()).isEqualTo("env2_production");
    assertThat(analysisMetadataEntity.getTenant()).isEqualTo("env2_production");
  }

  @Test
  void shouldReturnCorrectTenantId() {
    analysisMetadataRepository.save(ANALYSIS_METADATA);

    assertThat(underTest.getTenantIdByAnalysis(ANALYSIS)).isEqualTo(TENANT);
  }

  @Test
  void shouldReturnCorrectMetadata() {
    analysisMetadataRepository.save(ANALYSIS_METADATA);

    AnalysisMetadataDto analysisMetadata = underTest.getAnalysisMetadata(ANALYSIS);

    assertThat(analysisMetadata.getTenant()).isEqualTo(TENANT);
    assertThat(analysisMetadata.getElasticIndexName()).isEqualTo(ELASTIC_INDEX_PATTERN);
  }
}
