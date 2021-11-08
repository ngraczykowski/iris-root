package com.silenteight.simulator.retention.dataset.amqp.listener;

import lombok.NonNull;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.dataretention.api.v1.DatasetsExpired;

public interface DatasetsExpiredMessageHandler {

  AnalysisExpired handle(@NonNull DatasetsExpired request);
}
