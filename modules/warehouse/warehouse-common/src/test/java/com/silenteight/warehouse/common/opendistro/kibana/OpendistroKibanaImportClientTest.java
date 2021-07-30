package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.InputStream;

import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.KIBANA_INDEX_PATTERN;
import static com.silenteight.warehouse.common.opendistro.kibana.SavedObjectType.SEARCH;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_KIBANA_INDEX_PATTERN_NAME;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.SAVED_SEARCH;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroKibanaTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
@Slf4j
class OpendistroKibanaImportClientTest {

  private static final String IMPORT_FILE =
      "local_production_ai_reasoning.20212807-090601.so.ndjson";

  @Autowired
  private OpendistroSavedObjectsLoader underTest;

  @Autowired
  private OpendistroKibanaClientFactory opendistroKibanaClientFactory;

  private OpendistroKibanaClient opendistroKibanaClient;

  @BeforeEach
  void init() {
    opendistroKibanaClient = opendistroKibanaClientFactory.getAdminClient();
  }

  @AfterEach
  void cleanup() {
    safeDeleteObject(KIBANA_INDEX_PATTERN, PRODUCTION_KIBANA_INDEX_PATTERN_NAME);
    safeDeleteObject(SEARCH, SAVED_SEARCH);
  }

  @Test
  @SneakyThrows
  void shouldImportSavedObjects() {
    InputStream kibanaSavedObjectImportPayload = loadLocalFile(IMPORT_FILE);

    underTest.loadAll(ADMIN_TENANT, kibanaSavedObjectImportPayload);

    assertThat(getKibanaIndexPatternsCount(ADMIN_TENANT)).isEqualTo(1);
    assertThat(getSavedSearchCount(ADMIN_TENANT)).isEqualTo(1);
  }

  private InputStream loadLocalFile(String name) {
    return getClass().getClassLoader().getResourceAsStream(name);
  }

  private int getKibanaIndexPatternsCount(String tenant) {
    return opendistroKibanaClient.listKibanaIndexPattern(tenant, 100).size();
  }

  private int getSavedSearchCount(String tenant) {
    return opendistroKibanaClient.listSavedSearchDefinitions(tenant, 100).size();
  }

  private void safeDeleteObject(SavedObjectType type, String id) {
    try {
      opendistroKibanaClient.deleteSavedObjects(ADMIN_TENANT, type, id);
    } catch (OpendistroKibanaClientException e) {
      log.debug("item not present type={}, id={}", type, id);
    }
  }
}
