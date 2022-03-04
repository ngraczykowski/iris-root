package com.silenteight.fab.dataprep.infrastructure.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.proto.fab.api.v1.AlertDetailsServiceGrpc.AlertDetailsServiceBlockingStub;
import com.silenteight.proto.fab.api.v1.AlertsDetailsRequest;
import com.silenteight.proto.fab.api.v1.AlertsDetailsResponse;

import static lombok.AccessLevel.PACKAGE;

@Slf4j
@RequiredArgsConstructor(access = PACKAGE)
public class AlertDetailsServiceClient {

  private final AlertDetailsServiceBlockingStub blockingStub;

  public AlertsDetailsResponse get(AlertsDetailsRequest request) {
    return blockingStub.alertsDetails(request);
  }

}
