package com.silenteight.customerbridge.common.alertmodel;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.serp.v1.alert.AlertModel;
import com.silenteight.proto.serp.v1.alert.AlertModel.Builder;

import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Message;

import java.util.Optional;

import static com.silenteight.sep.base.common.util.ReflectionUtils.invokeStaticGetter;

@RequiredArgsConstructor
@Slf4j
class AlertModelFactory {

  @NonNull
  private final Iterable<Class<? extends Message>> messageTypes;

  AlertModel get() {
    Builder builder = AlertModel.newBuilder();

    for (Class<? extends Message> type : messageTypes) {
      Optional<Object> maybeDescriptor = invokeStaticGetter(type, "getDescriptor");
      if (maybeDescriptor.isEmpty()) {
        log.warn("Message type without descriptor: class={}", type);
        continue;
      }

      Descriptor descriptor = (Descriptor) maybeDescriptor.get();  // NOSONAR false positive
      builder
          .addAlertDescriptorsBuilder()
          .setFullName(descriptor.getFullName())
          .setMessageDescriptor(descriptor.toProto());
    }

    return builder.build();
  }
}
