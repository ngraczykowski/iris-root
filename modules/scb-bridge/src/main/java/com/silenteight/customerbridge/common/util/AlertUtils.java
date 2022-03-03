package com.silenteight.customerbridge.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.common.ObjectId;
import com.silenteight.protocol.utils.Uuids;

import javax.annotation.Nonnull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AlertUtils {

  @Nonnull
  public static String getDebugId(Alert alert) {
    return getDebugId(alert.getId());
  }

  @Nonnull
  public static String getDebugId(ObjectId objectId) {
    String uniqueId =
        (objectId.hasId() && objectId.getId().getValue().size() == 16)
        ? Uuids.toJavaUuid(objectId.getId()).toString()
        : "<no-id>";
    String sourceId = objectId.getSourceId();
    String discriminator = objectId.getDiscriminator();

    StringBuilder builder = new StringBuilder();

    if (!sourceId.isEmpty())
      builder.append(sourceId).append("|");

    builder.append(uniqueId);

    if (!discriminator.isEmpty())
      builder.append("[").append(discriminator).append("]");

    return builder.toString();
  }
}
