package com.silenteight.sep.base.common.messaging;

import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

final class Lz4Utils {

  static XXHash32 createHash() {
    return XXHashFactory.fastestInstance().hash32();
  }

  private Lz4Utils() {
  }
}
