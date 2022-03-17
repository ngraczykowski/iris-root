package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.fab.api.v1.AlertMessageDetailsServiceGrpc.AlertMessageDetailsServiceBlockingStub;
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsRequest;
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
public class AlertDetailsServiceClient {

  private final AlertMessageDetailsServiceBlockingStub blockingStub;

  private final Duration deadline;

  public AlertMessagesDetailsResponse get(AlertMessagesDetailsRequest request) {
    return blockingStub.withDeadlineAfter(deadline.getSeconds(), SECONDS).alertsDetails(request);
  }

}
