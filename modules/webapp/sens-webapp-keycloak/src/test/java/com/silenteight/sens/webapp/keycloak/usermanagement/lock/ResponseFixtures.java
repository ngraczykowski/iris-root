package com.silenteight.sens.webapp.keycloak.usermanagement.lock;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.serverError;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ResponseFixtures {

  static final Response OK = ok().build();

  static final Response SERVER_ERROR = serverError().build();
}
