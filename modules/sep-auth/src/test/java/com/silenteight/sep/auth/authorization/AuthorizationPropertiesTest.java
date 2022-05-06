package com.silenteight.sep.auth.authorization;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.silenteight.sep.auth.authorization.AuthorizationProperties.DEFAULT_PERMIT_ALL_URL;
import static com.silenteight.sep.auth.authorization.AuthorizationProperties.DEFAULT_PRINCIPAL_CLAIM;
import static java.util.Set.of;
import static org.assertj.core.api.Assertions.*;

class AuthorizationPropertiesTest {

  @Nested
  @SpringBootTest(classes = {
      com.silenteight.sep.auth.authorization.AuthorizationPropertiesTest.TestConfiguration.class })
  @ActiveProfiles("properties-override")
  class OverrideValueTest {

    @Autowired
    private AuthorizationProperties properties;

    @Test
    void whenPrincipalClaimProvided_thenIsReturned() {
      assertThat(properties.getPrincipalClaim()).isEqualTo("sub");
    }

    @Test
    void whenPermitAllUrlsProvided_thenIsReturned() {
      assertThat(properties.getPermitAllUrls()).isEqualTo(
          new String[] { "/openapi/**", "/management/**" });
    }

  }

  @Nested
  @SpringBootTest(classes = {
      com.silenteight.sep.auth.authorization.AuthorizationPropertiesTest.TestConfiguration.class })
  class DefaultValueTest {

    private static final String PERMISSION_1 = "PERMISSION_1";
    private static final String PERMISSION_2 = "PERMISSION_2";
    private static final String METHOD_1 = "METHOD_1";
    private static final String METHOD_2 = "METHOD_2";
    private static final String METHOD_3 = "METHOD_3";
    private static final String ROLE_1 = "ROLE_1";
    private static final String ROLE_2 = "ROLE_2";

    @Autowired
    private AuthorizationProperties properties;

    @Test
    void whenMappingsAreProvided_getPermissionsFromProperties() {
      assertThat(properties.permissionsByMethod()).isEqualTo(
          Map.of(
              METHOD_1,
              of(PERMISSION_1),
              METHOD_2,
              of(PERMISSION_1, PERMISSION_2),
              METHOD_3,
              of(PERMISSION_2)));
      assertThat(properties.permissionsByRole()).isEqualTo(Map.of(
          ROLE_1,
          of(PERMISSION_1),
          ROLE_2,
          of(PERMISSION_2)));
    }

    @Test
    void whenPrincipalClaimNotProvided_thenDefaultIsReturned() {
      assertThat(properties.getPrincipalClaim()).isEqualTo(DEFAULT_PRINCIPAL_CLAIM);
    }

    @Test
    void whenPermitAllUrlsNotProvided_thenDefaultIsReturned() {
      assertThat(properties.getPermitAllUrls()).isEqualTo(DEFAULT_PERMIT_ALL_URL);
    }

  }

  @EnableConfigurationProperties(AuthorizationProperties.class)
  static class TestConfiguration {
  }

}
