package com.silenteight.customerbridge.common.hitdetails.builder.consumerrepo;

import lombok.NonNull;

import java.util.Optional;
import java.util.function.BiConsumer;

public interface ConsumerRepository<A, B> {

  Optional<BiConsumer<A, B>> find(@NonNull String key);
}