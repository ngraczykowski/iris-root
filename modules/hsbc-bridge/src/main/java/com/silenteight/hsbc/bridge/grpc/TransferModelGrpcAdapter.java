package com.silenteight.hsbc.bridge.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.transfer.ModelClient.Model;
import com.silenteight.hsbc.bridge.transfer.TransferClient;

import io.grpc.StatusRuntimeException;
import org.springframework.retry.annotation.Retryable;

@RequiredArgsConstructor
public class TransferModelGrpcAdapter implements TransferClient {

  private final long deadlineInSeconds;

  @Override
  @Retryable(value = StatusRuntimeException.class)
  public void transfer(Model model) {
    // TODO GRPC .proto contract should be defined by Governance soon !!!
    throw new UnsupportedOperationException();
  }
}
