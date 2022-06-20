/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import lombok.NonNull;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertIdWithDetails;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import javax.validation.constraints.Size;

public interface AlertInFlightService {

  void saveUniqueAlerts(
      @Size(max = 10_000) Collection<AlertId> alerts, ScbAlertIdContext alertIdContext);

  void delete(@NonNull AlertId alertId);

  void deleteExpired(@NonNull OffsetDateTime expireDate);

  void deleteAlerts(List<AlertId> alertIds);

  void ack(@NonNull AlertId alertId);

  void error(@NonNull AlertId alertId, @NonNull String error);

  List<AlertIdWithDetails> readChunk();

  long getAckAlertsCount();
}
