package com.silenteight.universaldatasource.app.feature.port.incoming;

import lombok.Value;

import com.google.protobuf.Message;

@Value
public class BatchFeatureInputResponse {

  // BatchGetMatchLocationInputsResponse, BatchGetMatchNameInputsResponse, etc.
  Message batchResponse;

  public <T extends Message> T castResponse(Class<T> type) {
    return type.cast(batchResponse);
  }
}
