package com.silenteight.protocol.utils;

import lombok.NonNull;

import com.silenteight.proto.protobuf.Uuid;

import com.google.protobuf.ByteString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.UUID;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Utilities to help create/manipulate silenteight/protobuf/uuid.proto.
 */
public final class Uuids {

  @Nonnull
  public static UUID toJavaUuid(@NonNull Uuid uuidProto) {
    ByteString value = uuidProto.getValue();

    checkArgument(value != null && !value.isEmpty(), "Uuid has no value!");
    checkArgument(
        value.size() == 16, "Uuid has invalid size of the value! Expected 16, got %d.",
        value.size());

    ByteBuffer buffer = value.asReadOnlyByteBuffer();

    buffer.order(ByteOrder.BIG_ENDIAN);
    long mostSigBits = buffer.getLong();
    long leastSigBits = buffer.getLong();

    return new UUID(mostSigBits, leastSigBits);
  }

  @Nonnull
  public static Uuid random() {
    return fromJavaUuid(UUID.randomUUID());
  }

  @Nonnull
  public static Uuid fromJavaUuid(@NonNull UUID uuid) {
    ByteBuffer uuidBuffer = ByteBuffer.wrap(new byte[16]);

    uuidBuffer.order(ByteOrder.BIG_ENDIAN);
    uuidBuffer.putLong(uuid.getMostSignificantBits());
    uuidBuffer.putLong(uuid.getLeastSignificantBits());
    uuidBuffer.flip();

    return Uuid.newBuilder().setValue(ByteString.copyFrom(uuidBuffer)).build();
  }

  private Uuids() {
  }
}
