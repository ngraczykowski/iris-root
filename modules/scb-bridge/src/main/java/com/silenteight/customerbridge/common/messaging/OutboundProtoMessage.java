package com.silenteight.customerbridge.common.messaging;

import lombok.*;
import lombok.Builder.Default;

import com.google.protobuf.Message;

import java.util.Optional;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@Value
@AllArgsConstructor
@EqualsAndHashCode(doNotUseGetters = true)
@ToString(doNotUseGetters = true)
@Builder
public class OutboundProtoMessage {

  @Nullable
  String exchange;

  @Nullable
  String routingKey;

  @NonNull
  Message message;

  @Default
  int priority = 1;

  public Optional<String> getExchange() {
    return ofNullable(exchange);
  }

  public Optional<String> getRoutingKey() {
    return ofNullable(routingKey);
  }
}
