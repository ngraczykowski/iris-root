package com.silenteight.serp.governance.common.signature;

import lombok.Value;

import com.google.protobuf.ByteString;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@Value
public class Signature {

  ByteString value;

  public String asString() {
    return toBase64String(value);
  }
}
