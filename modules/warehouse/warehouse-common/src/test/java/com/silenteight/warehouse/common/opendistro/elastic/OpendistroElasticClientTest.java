package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.SneakyThrows;

import com.silenteight.warehouse.common.opendistro.elastic.RoleDto.IndexPermission;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroKibanaContainer.OpendistroKibanaContainerInitializer;
import com.silenteight.warehouse.common.testing.elasticsearch.SimpleElasticTestClient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Set;

import static com.silenteight.warehouse.common.testing.elasticsearch.ElasticSearchTestConstants.PRODUCTION_ELASTIC_INDEX_NAME;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE_STRING;
import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes = OpendistroElasticTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroKibanaContainerInitializer.class,
    OpendistroElasticContainerInitializer.class
})
class OpendistroElasticClientTest {

  private static final String TEST_TENANT_ID = "itest_simulation_master";
  private static final String TEST_TENANT_DESCRIPTION = "description";

  private static final RoleDto ROLE_DTO = RoleDto.builder()
      .indexPermissions(List.of(IndexPermission.builder()
          .indexPatterns(List.of("itest_production"))
          .dls("{\"terms\": {\"s8_country.keyword\": [\"UK\", \"ES\"]}}")
          .build()))
      .build();

  private static final RoleMappingDto ROLE_MAPPING_DTO = RoleMappingDto.builder()
      .backendRoles(Set.of(ELASTIC_ALLOWED_ROLE_STRING))
      .build();

  @Autowired
  OpendistroElasticClient opendistroElasticClient;

  @Autowired
  SimpleElasticTestClient simpleElasticTestClient;

  @BeforeEach
  void setUp() {
    saveRole();
    saveRoleMapping();
  }

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
    RoleDto responseRoleDto =
        opendistroElasticClient.getCurrentRole(ELASTIC_ALLOWED_ROLE_STRING);

    assertThat(responseRoleDto).isEqualTo(ROLE_DTO);
  }

  @Test
  @SneakyThrows
  void shouldReturnRoleMapping() {
    opendistroElasticClient.setRoleMapping(ELASTIC_ALLOWED_ROLE_STRING, ROLE_MAPPING_DTO);

    RoleMappingDto responseRoleDto =
        opendistroElasticClient.getRoleMapping(ELASTIC_ALLOWED_ROLE_STRING);

    assertThat(responseRoleDto).isEqualTo(ROLE_MAPPING_DTO);
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

  @Test
  void shouldExecuteQueryWithFields() {
    simpleElasticTestClient.storeData(
        PRODUCTION_ELASTIC_INDEX_NAME,
        "123",
        of("feature/name", "feature value", "key", "value"));

    QueryDto queryDto = QueryDto
        .builder()
        .query("select `feature/name` from " + PRODUCTION_ELASTIC_INDEX_NAME)
        .build();
    QueryResultDto queryResultDto = opendistroElasticClient.executeSql(queryDto);

    assertThat(queryResultDto).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenRoleNotFound() {
    //when
    opendistroElasticClient.removeRole(ELASTIC_ALLOWED_ROLE_STRING);

    //then
    assertThatThrownBy(() -> opendistroElasticClient.getCurrentRole(ELASTIC_ALLOWED_ROLE_STRING))
        .isInstanceOf(OpendistroElasticClientException.class)
        .hasMessageContaining("Error while calling getCurrentRole ES response: 404: Not Found");
  }

  @Test
  void shouldRemoveRoleMapping() {
    //when
    opendistroElasticClient.removeRoleMapping(ELASTIC_ALLOWED_ROLE_STRING);

    //then
    assertThatThrownBy(() -> opendistroElasticClient.getRoleMapping(ELASTIC_ALLOWED_ROLE_STRING))
        .isInstanceOf(OpendistroElasticClientException.class)
        .hasMessageContaining("Error while calling getRoleMapping ES response: 404: Not Found");
  }

  private void saveRole() {
    opendistroElasticClient.setRole(ELASTIC_ALLOWED_ROLE_STRING, ROLE_DTO);
  }

  private void saveRoleMapping() {
    opendistroElasticClient.setRoleMapping(ELASTIC_ALLOWED_ROLE_STRING, ROLE_MAPPING_DTO);
  }
}
