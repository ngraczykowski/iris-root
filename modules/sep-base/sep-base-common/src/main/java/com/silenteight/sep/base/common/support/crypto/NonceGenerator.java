package com.silenteight.sep.base.common.support.crypto;

import java.util.function.Supplier;

public interface NonceGenerator extends Supplier<byte[]> {

  default byte[] get() {
    return generate();
  }

  byte[] generate();
}
