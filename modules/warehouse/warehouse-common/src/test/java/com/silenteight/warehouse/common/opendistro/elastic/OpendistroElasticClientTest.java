package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.opendistro.elastic.RoleDto.IndexPermission;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE;
import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroElasticTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroKibanaContainerInitializer.class,
    OpendistroElasticContainerInitializer.class
})
class OpendistroElasticClientTest {

  private static final String TEST_TENANT_ID = "itest_simulation_master";
  private static final String TEST_TENANT_DESCRIPTION = "description";

  @Autowired
  OpendistroElasticClient opendistroElasticClient;

  @Autowired
  SimpleElasticTestClient simpleElasticTestClient;

  @Test
  @SneakyThrows
  void shouldReturnAddedTenant() {
    //given
    opendistroElasticClient.createTenant(TEST_TENANT_ID, TEST_TENANT_DESCRIPTION);

    //when
    var tenantsNameList = opendistroElasticClient.getTenantsList();

    //then
    assertThat(tenantsNameList).contains(TEST_TENANT_ID);
  }

  @Test
  @SneakyThrows
  void shouldReturnRole() {
    RoleDto roleDto = RoleDto.builder()
        .indexPermissions(List.of(IndexPermission.builder()
            .indexPatterns(List.of("itest_production"))
            .dls("{\"terms\": {\"s8_country.keyword\": [\"UK\", \"ES\"]}}")
            .build()))
        .build();

    opendistroElasticClient.setRole(ELASTIC_ALLOWED_ROLE.toString(), roleDto);

    RoleDto responseRoleDto =
        opendistroElasticClient.getCurrentRole(ELASTIC_ALLOWED_ROLE.toString());

    assertThat(responseRoleDto).isEqualTo(roleDto);
  }

  @Test
  void shouldExecuteQuery() {
    simpleElasticTestClient.storeData(PRODUCTION_ELASTIC_INDEX_NAME, "123", emptyMap());

    QueryDto queryDto = QueryDto.builder()
        .query("select * from " + PRODUCTION_ELASTIC_INDEX_NAME)
        .build();
    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);

    assertThat(queryResultDto).isNotNull();
  }
}
