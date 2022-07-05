package com.silenteight.agent.common.messaging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class Lz4Utils {

  static XXHash32 createHash() {
    return XXHashFactory.fastestInstance().hash32();
  }
}
