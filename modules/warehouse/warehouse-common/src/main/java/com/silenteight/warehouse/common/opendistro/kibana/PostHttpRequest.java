package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.annotation.Nullable;

import static java.util.Objects.nonNull;

@Value
@Builder
class PostHttpRequest {

  @NonNull
  String endpoint;
  @NonNull
  String tenant;
  @Nullable
  byte[] payload;
  @Nullable
  String origin;

  boolean hasPayload() {
    return nonNull(payload);
  }

  InputStream getPayload() {
    return new ByteArrayInputStream(payload);
  }
}
