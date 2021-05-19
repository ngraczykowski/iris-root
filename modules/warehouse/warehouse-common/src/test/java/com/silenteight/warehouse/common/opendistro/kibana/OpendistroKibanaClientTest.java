package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexFixture.KIBANA_INDEX_ID;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexFixture.KIBANA_INDEX_PATTERN_ATTRIBUTES;
import static com.silenteight.warehouse.common.opendistro.kibana.ReportDefinitionFixture.REPORT_DEFINITION;
import static com.silenteight.warehouse.common.opendistro.kibana.ReportDefinitionFixture.REPORT_NAME;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.opendistro.kibana.SearchFixture.SAVED_SEARCH_ATTRIBUTES;
import static com.silenteight.warehouse.common.opendistro.kibana.SearchFixture.SAVED_SEARCH_REFERENCES;
import static com.silenteight.warehouse.common.opendistro.kibana.SearchFixture.SEARCH_ATTRIBUTES_COLUMNS;
import static com.silenteight.warehouse.common.opendistro.kibana.SearchFixture.SEARCH_ID;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = OpendistroKibanaTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
@Slf4j
class OpendistroKibanaClientTest {

  @Autowired
  OpendistroKibanaClient opendistroKibanaClient;

  @AfterEach
  public void cleanup() {
    safeDeleteObject(KIBANA_INDEX_PATTERN, KIBANA_INDEX_ID);
    safeDeleteObject(SEARCH, SEARCH_ID);
  }

  @Test
  void shouldCreateKibanaIndexPattern() {
    KibanaIndexPatternDto kibanaIndexPattern = KibanaIndexPatternDto.builder()
        .id(KIBANA_INDEX_ID)
        .attributes(KIBANA_INDEX_PATTERN_ATTRIBUTES)
        .build();

    opendistroKibanaClient.createKibanaIndexPattern(ADMIN_TENANT, kibanaIndexPattern);

    List<KibanaIndexPatternDto> savedObjects =
        opendistroKibanaClient.listKibanaIndexPattern(ADMIN_TENANT, 20);

    assertThat(savedObjects).hasSize(1);
    KibanaIndexPatternDto savedObject = savedObjects.get(0);
    assertThat(savedObject.getId()).isEqualTo(KIBANA_INDEX_ID);
    assertThat(savedObject.getAttributes()).isEqualTo(KIBANA_INDEX_PATTERN_ATTRIBUTES);
  }

  @Test
  void shouldReturnAddedSearchDefinition() {
    SearchDto savedSearchObject = SearchDto.builder()
        .id(SEARCH_ID)
        .attributes(SAVED_SEARCH_ATTRIBUTES)
        .references(of(SAVED_SEARCH_REFERENCES))
        .build();
    opendistroKibanaClient.createSavedSearchObjects(ADMIN_TENANT, savedSearchObject);

    List<SearchDto> searchDtos =
        opendistroKibanaClient.listSavedSearchDefinitions(ADMIN_TENANT, 20);

    assertThat(searchDtos).hasSize(1);
    SearchDto searchDto = searchDtos.get(0);
    assertThat(searchDto.getReferences()).hasSize(1);
    assertThat(searchDto.getAttributes().getColumns())
        .containsExactlyInAnyOrderElementsOf(SEARCH_ATTRIBUTES_COLUMNS);
  }

  @Test
  void shouldCreateReportDefinition() {
    createSearchDefinition(SEARCH_ID);

    ReportDefinitionDto reportDefinition = ReportDefinitionDto.builder()
        .reportDefinitionDetails(REPORT_DEFINITION)
        .build();

    String reportDefinitionId =
        opendistroKibanaClient.createReportDefinition(ADMIN_TENANT, reportDefinition);

    await()
        .atMost(5, SECONDS)
        .until(() -> opendistroKibanaClient.isExistingReportDefinition(
            ADMIN_TENANT, reportDefinitionId));

    List<ReportDefinitionDto> reportDefinitions =
        opendistroKibanaClient.listReportDefinitions(ADMIN_TENANT);
    assertThat(reportDefinitions).hasSize(1);
    ReportDefinitionDto reportDefinitionDto = reportDefinitions.get(0);
    assertThat(reportDefinitionDto.getReportName()).isEqualTo(REPORT_NAME);
  }

  private void createSearchDefinition(String id) {
    SearchDto savedSearchObject = SearchDto.builder()
        .id(id)
        .attributes(SAVED_SEARCH_ATTRIBUTES)
        .references(of(SAVED_SEARCH_REFERENCES))
        .build();
    opendistroKibanaClient.createSavedSearchObjects(ADMIN_TENANT, savedSearchObject);
  }

  private void safeDeleteObject(SavedObjectType type, String id) {
    try {
      opendistroKibanaClient.deleteSavedObjects(ADMIN_TENANT, type, id);
    } catch (OpendistroKibanaClientException e) {
      log.debug("item not present type={}, id={}", type, id);
    }
  }
}
