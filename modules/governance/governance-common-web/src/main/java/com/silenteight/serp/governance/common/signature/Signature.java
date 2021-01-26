package com.silenteight.serp.governance.common.signature;

import lombok.Value;

import com.google.protobuf.ByteString;

@Value
public class Signature {

  ByteString value;
}
