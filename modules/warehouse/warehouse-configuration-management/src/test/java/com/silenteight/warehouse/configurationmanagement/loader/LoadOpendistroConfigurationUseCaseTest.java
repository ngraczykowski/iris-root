package com.silenteight.warehouse.configurationmanagement.loader;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.kibana.*;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.internal.v1.ImportConfigurationRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.io.InputStream;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest(classes = LoadOpendistroConfigurationTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
@Slf4j
class LoadOpendistroConfigurationUseCaseTest {

  private static final String IMPORT_FILE = "dev_production_ai_reasoning.20212907-114915.json";

  @Autowired
  LoadOpendistroConfigurationUseCase underTest;

  @Autowired
  OpendistroKibanaClientFactory opendistroKibanaClientFactory;

  private OpendistroKibanaClient opendistroKibanaClient;

  @BeforeEach
  void init() {
    opendistroKibanaClient = opendistroKibanaClientFactory.getAdminClient();
  }

  @Test
  @SneakyThrows
  void shouldLoadSavedObjectsAndReports() {
    ImportConfigurationRequest validRequest = getValidRequest();

    underTest.load(validRequest);

    List<KibanaIndexPatternDto> indexPatterns =
        opendistroKibanaClient.listKibanaIndexPattern(validRequest.getTenant(), 10);
    assertThat(indexPatterns)
        .extracting(KibanaIndexPatternDto::getId)
        .containsExactly("all_production");

    List<SearchDto> searches =
        opendistroKibanaClient.listSavedSearchDefinitions(validRequest.getTenant(), 10);
    assertThat(searches)
        .extracting(SearchDto::getId)
        .containsExactly("ai-resoning-wl-search");

    await()
        .atMost(5, SECONDS)
        .until(() -> getReportCount(validRequest.getTenant()) > 0);
  }

  @SneakyThrows
  ImportConfigurationRequest getValidRequest() {
    InputStream resourceAsStream = getClass()
        .getClassLoader()
        .getResourceAsStream(IMPORT_FILE);
    TypeReference<ConfigurationImportDefinition> typeRef = new TypeReference<>() {};

    ConfigurationImportDefinition configurationImportDefinition
        = new ObjectMapper().readValue(resourceAsStream, typeRef);

    return ImportConfigurationRequest.newBuilder()
        .setTenant(configurationImportDefinition.getTenant())
        .setSavedObjects(configurationImportDefinition.getSavedObjects())
        .setReportInstances(configurationImportDefinition.getReportInstances())
        .build();
  }

  private int getReportCount(String tenant) {
    return opendistroKibanaClient.listReportDefinitions(tenant).size();
  }
}
