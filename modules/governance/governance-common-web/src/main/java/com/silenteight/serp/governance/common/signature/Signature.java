package com.silenteight.serp.governance.common.signature;

import lombok.Value;

import com.google.protobuf.ByteString;

import static com.silenteight.sep.base.common.protocol.ByteStringUtils.fromBase64String;
import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;

@Value
public class Signature {

  ByteString value;

  public static Signature fromBase64(String string) {
    return new Signature(fromBase64String(string));
  }

  @Override
  public String toString() {
    return "Signature{value=" + asString() + "}";
  }

  public String asString() {
    return toBase64String(value);
  }

  public boolean isEqualTo(ByteString byteString) {
    return value.equals(byteString);
  }
}
