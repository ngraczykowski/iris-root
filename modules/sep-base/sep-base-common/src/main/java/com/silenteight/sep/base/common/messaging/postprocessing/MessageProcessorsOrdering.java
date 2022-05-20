package com.silenteight.sep.base.common.messaging.postprocessing;

class MessageProcessorsOrdering {

  private MessageProcessorsOrdering() {
  }

  static final int ENCRYPTION = 1;
  static final int COMPRESSION = 2;
  static final int DECOMPRESSION = 3;
  static final int DECRYPTION = 4;
}
