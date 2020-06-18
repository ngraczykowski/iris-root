package com.silenteight.sens.webapp.backend.changerequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.CORRELATION_ID_HEADER;
import static java.util.UUID.randomUUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RestControllerTestUtils {

  public static Map<String, UUID> defaultHeaders() {
    return Map.of(CORRELATION_ID_HEADER, randomUUID());
  }
}
