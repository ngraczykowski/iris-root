package com.silenteight.warehouse.common.testing.rest;

import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_PASSWORD;
import static com.silenteight.warehouse.common.testing.rest.TestCredentials.ELASTIC_ALLOWED_USERNAME;

@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(username = ELASTIC_ALLOWED_USERNAME, password = ELASTIC_ALLOWED_PASSWORD)
public @interface WithElasticAccessCredentials {
}
