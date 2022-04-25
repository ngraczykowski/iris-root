package com.silenteight.warehouse.simulation.processing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.DataIndexResponse;
import com.silenteight.data.api.v2.SimulationAlert;
import com.silenteight.data.api.v2.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.handler.SimulationRequestV2CommandHandler;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertV2MappingService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchInsertService;

import java.util.Collection;
import java.util.List;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertV2UseCase implements SimulationRequestV2CommandHandler {

  @NonNull
  private final SimulationDbPartitionFactory simulationPartitionFactory;

  @NonNull
  private final SimulationAlertV2MappingService simulationAlertV2MappingService;

  @NonNull
  private final SimulationAlertInsertService simulationAlertInsertService;

  @NonNull
  private final SimulationMatchInsertService simulationMatchInsertService;

  @NonNull
  private final TimeSource timeSource;

  private final int simulationAlertsBatchSize;

  @Override
  public DataIndexResponse handle(SimulationDataIndexRequest request) {

    log.info("v2.SimulationDataIndexRequest received, requestId={}, analysisName={}, "
            + "alertCount={}, sizeInBytes={}",
        request.getRequestId(), request.getAnalysisName(),
        request.getAlertsCount(), request.getSerializedSize());

    simulationPartitionFactory.createDbPartition(request.getAnalysisName());

    partition(request.getAlertsList(), simulationAlertsBatchSize).stream()
        .map((List<SimulationAlert> alerts) -> simulationAlertV2MappingService.mapAlerts(
            alerts,
            request.getAnalysisName()))
        .forEach(this::insert);

    log.debug("v2.SimulationDataIndexRequest processed, requestId={}", request.getRequestId());

    return DataIndexResponse.newBuilder()
        .setRequestId(request.getRequestId())
        .setIndexTime(toTimestamp(timeSource.now()))
        .build();
  }

  private void insert(Collection<SimulationAlertDefinition> simulationAlerts) {
    simulationAlertInsertService.insert(simulationAlerts);

    List<SimulationMatchDefinition> simulationMatchDefinitions = simulationAlerts
        .stream()
        .flatMap(alert -> alert.getMatches().stream())
        .collect(toList());
    simulationMatchInsertService.insert(simulationMatchDefinitions);
  }
}
