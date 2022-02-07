package com.silenteight.warehouse.common.opendistro.elastic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.common.opendistro.elastic.RoleDto.IndexPermission;
import com.silenteight.warehouse.common.opendistro.elastic.exception.OpendistroElasticClientException;
import com.silenteight.warehouse.common.testing.elasticsearch.OpendistroElasticContainer.OpendistroElasticContainerInitializer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_ROLE_STRING;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(classes = OpendistroElasticTestConfiguration.class)
@ContextConfiguration(initializers = {
    OpendistroElasticContainerInitializer.class
})
class OpendistroElasticClientTest {

  private static final RoleDto ROLE_DTO = RoleDto.builder()
      .indexPermissions(List.of(IndexPermission.builder()
          .indexPatterns(List.of("itest_production*"))
          .dls("{\"terms\": {\"s8_country.keyword\": [\"UK\", \"ES\"]}}")
          .build()))
      .build();

  @Autowired
  OpendistroElasticClient opendistroElasticClient;

  @BeforeEach
  void setUp() {
    saveRole();
  }

  @Test
  @SneakyThrows
  void shouldReturnRole() {
    RoleDto responseRoleDto =
        opendistroElasticClient.getCurrentRole(ELASTIC_ALLOWED_ROLE_STRING);

    assertThat(responseRoleDto).isEqualTo(ROLE_DTO);
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

  private void saveRole() {
    opendistroElasticClient.setRole(ELASTIC_ALLOWED_ROLE_STRING, ROLE_DTO);
  }
}
