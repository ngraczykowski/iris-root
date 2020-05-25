package com.silenteight.serp.common.protocol;

import com.google.protobuf.*;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

class MessageRegistryTest {

  private final MessageRegistry underTest =
      new MessageRegistry(asList(Timestamp.class, Duration.class));

  @Test
  void unpacksMessageOfCorrectType() {
    Any packedAlert = Any.pack(Timestamp.newBuilder().setNanos(12345).build());

    Optional<Message> message = underTest.maybeUnpackAny(packedAlert);

    assertThat(message).isNotEmpty().containsInstanceOf(Timestamp.class);
  }

  @Test
  void returnsEmptyWhenUnknownType() {
    Any packedUuid = Any.pack(Value.newBuilder().setBoolValue(true).build());

    Optional<Message> message = underTest.maybeUnpackAny(packedUuid);

    assertThat(message).isEmpty();
  }

  @Test
  void invalidAnyMessageThrows() {
    assertThatThrownBy(() -> underTest.maybeUnpackAny(Any.newBuilder().build()))
        .isInstanceOf(IllegalArgumentException.class);

    assertThatThrownBy(
        () -> underTest.maybeUnpackAny(Any.newBuilder().setTypeUrl("bogus type").build()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void formatsAsJson() throws InvalidProtocolBufferException {
    Timestamp timestamp = Timestamp.newBuilder()
        .setSeconds(12345L)
        .setNanos(6789)
        .build();

    String json = underTest.toJson(timestamp);

    assertThat(json).isEqualTo("\"1970-01-01T03:25:45.000006789Z\"");
  }
}
