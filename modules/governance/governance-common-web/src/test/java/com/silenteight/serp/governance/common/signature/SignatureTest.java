package com.silenteight.serp.governance.common.signature;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SignatureTest {

  private static final ByteString SIGNATURE_VALUE_1 = ByteString.copyFromUtf8("SIGNATURE_1");

  private static final ByteString SIGNATURE_VALUE_2 = ByteString.copyFromUtf8("SIGNATURE_2");


  @Test
  void equalShouldCompareSignatureValue() {
    Signature signature =  new Signature(SIGNATURE_VALUE_1);

    assertThat(signature.isEqualTo(SIGNATURE_VALUE_1)).isTrue();
    assertThat(signature.isEqualTo(SIGNATURE_VALUE_2)).isFalse();
  }
}
