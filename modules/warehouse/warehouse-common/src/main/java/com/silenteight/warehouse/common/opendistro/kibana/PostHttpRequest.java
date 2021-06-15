package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.ByteArrayInputStream;
import java.net.http.HttpRequest.BodyPublisher;
import javax.annotation.Nullable;

import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpRequest.BodyPublishers.ofInputStream;
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

  BodyPublisher getBodyPublisher() {
    if (hasPayload()) {
      return ofInputStream(() -> new ByteArrayInputStream(payload));
    } else {
      return noBody();
    }
  }

  private boolean hasPayload() {
    return nonNull(payload);
  }
}
