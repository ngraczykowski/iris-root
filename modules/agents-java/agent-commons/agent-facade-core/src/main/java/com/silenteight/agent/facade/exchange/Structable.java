package com.silenteight.agent.facade.exchange;

import com.google.protobuf.Struct;

public interface Structable {

  default Struct toStruct() {
    return StructReasonMapper.toStruct(this);
  }
}
