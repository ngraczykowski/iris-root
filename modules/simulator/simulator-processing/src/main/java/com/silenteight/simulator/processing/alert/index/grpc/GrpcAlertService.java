
package com.silenteight.simulator.processing.alert.index.grpc;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Alert;
import com.silenteight.adjudication.api.v1.AlertServiceGrpc.AlertServiceBlockingStub;
import com.silenteight.adjudication.api.v1.GetAlertRequest;
import com.silenteight.simulator.processing.alert.index.feed.AlertService;

@RequiredArgsConstructor
class GrpcAlertService implements AlertService {

  @NonNull
  private final AlertServiceBlockingStub alertStub;

  @Override
  public Alert getAlert(@NonNull String alertName) {
    return alertStub.getAlert(toGetAlertRequest(alertName));
  }

  private static GetAlertRequest toGetAlertRequest(String alertName) {
    return GetAlertRequest.newBuilder()
        .setAlert(alertName)
        .build();
  }
}
