package com.silenteight.warehouse.simulation.processing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.handler.SimulationIndexRequestCommandHandler;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertMappingService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;

import java.util.List;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertIndexUseCase implements SimulationIndexRequestCommandHandler {

  @NonNull
  private final SimulationDbPartitionFactory simulationPartitionFactory;

  @NonNull
  private final SimulationAlertMappingService simulationAlertMappingService;

  @NonNull
  private final SimulationAlertInsertService simulationAlertInsertService;

  @NonNull
  private final TimeSource timeSource;

  private final int simulationAlertsBatchSize;

  @Override
  public DataIndexResponse handle(SimulationDataIndexRequest request) {
    log.info("SimulationDataIndexRequest received, requestId={}, analysisName={}, "
            + "alertCount={}, sizeInBytes={}",
        request.getRequestId(), request.getAnalysisName(),
        request.getAlertsCount(), request.getSerializedSize());

    simulationPartitionFactory.createDbPartition(request.getAnalysisName());

    partition(request.getAlertsList(), simulationAlertsBatchSize).stream()
        .map((List<Alert> alerts) -> simulationAlertMappingService.mapFields(alerts,
            request.getAnalysisName()))
        .forEach(simulationAlertInsertService::insert);

    log.debug("SimulationDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }
}
