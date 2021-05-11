package com.silenteight.warehouse.common.opendistro.kibana;

import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.google.common.collect.ImmutableList.of;
import static com.silenteight.warehouse.common.opendistro.kibana.KibanaIndexFixture.*;
import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.ADMIN_TENANT;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroKibanaTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class,
    OpendistroKibanaContainerInitializer.class
})
class OpendistroKibanaClientTest {

  @Autowired
  OpendistroKibanaClient opendistroKibanaClient;

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
    KibanaIndexPatternDto kibanaIndexPattern = KibanaIndexPatternDto.builder()
        .id("otherId")
        .attributes(KIBANA_INDEX_PATTERN_ATTRIBUTES)
        .build();
    opendistroKibanaClient.createKibanaIndexPattern(ADMIN_TENANT, kibanaIndexPattern);

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
}
