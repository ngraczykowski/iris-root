package com.silenteight.warehouse.common.testing.rest;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_FORBIDDEN_PASSWORD;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_FORBIDDEN_USERNAME;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = ELASTIC_FORBIDDEN_USERNAME, password = ELASTIC_FORBIDDEN_PASSWORD)
public @interface WithElasticForbiddenCredentials {
}
