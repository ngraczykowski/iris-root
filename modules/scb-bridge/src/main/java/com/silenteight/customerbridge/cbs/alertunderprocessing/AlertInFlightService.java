package com.silenteight.customerbridge.cbs.alertunderprocessing;

import lombok.NonNull;

import com.silenteight.customerbridge.cbs.alertid.AlertId;
import com.silenteight.customerbridge.cbs.alertid.AlertIdWithDetails;
import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Size;

public interface AlertInFlightService {

  void saveUniqueAlerts(
      @Size(max = 10_000) Collection<AlertId> alerts, ScbAlertIdContext alertIdContext);

  void delete(@NonNull AlertId alertId);

  void deleteExpired(@NonNull OffsetDateTime expireDate);

  void update(@NonNull AlertId alertId, @NonNull AlertUnderProcessing.State state);

  void update(
      @NonNull AlertId alertId, @NonNull AlertUnderProcessing.State state, @NonNull String error);

  List<AlertIdWithDetails> readChunk();
}
