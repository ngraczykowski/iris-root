package com.silenteight.warehouse.simulation.processing;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.Alert;
import com.silenteight.data.api.v1.DataIndexResponse;
import com.silenteight.data.api.v1.SimulationDataIndexRequest;
import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.simulation.handler.SimulationRequestV1CommandHandler;
import com.silenteight.warehouse.simulation.processing.dbpartitioning.SimulationDbPartitionFactory;
import com.silenteight.warehouse.simulation.processing.mapping.SimulationAlertV1MappingService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationAlertInsertService;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchDefinition;
import com.silenteight.warehouse.simulation.processing.storage.SimulationMatchInsertService;

import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

import static com.silenteight.warehouse.common.time.Timestamps.toTimestamp;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.ListUtils.partition;

@Slf4j
@RequiredArgsConstructor
public class SimulationAlertV1UseCase implements SimulationRequestV1CommandHandler {

  @NonNull
  private final SimulationDbPartitionFactory simulationPartitionFactory;

  @NonNull
  private final SimulationAlertV1MappingService simulationAlertV1MappingService;

  @NonNull
  private final SimulationAlertInsertService simulationAlertInsertService;

  @NonNull
  private final SimulationMatchInsertService simulationMatchInsertService;

  @NonNull
  private final TimeSource timeSource;

  private final int simulationAlertsBatchSize;

  @Override
  @Transactional
  public DataIndexResponse handle(SimulationDataIndexRequest request) {
    log.info("v1.SimulationDataIndexRequest received, requestId={}, analysisName={}, "
            + "alertCount={}, sizeInBytes={}",
        request.getRequestId(), request.getAnalysisName(),
        request.getAlertsCount(), request.getSerializedSize());

    simulationPartitionFactory.createDbPartition(request.getAnalysisName());

    partition(request.getAlertsList(), simulationAlertsBatchSize).stream()
        .map((List<Alert> alerts) -> simulationAlertV1MappingService.mapFields(alerts,
            request.getAnalysisName()))
        .forEach(this::insert);

    log.debug("v1.SimulationDataIndexRequest processed, requestId={}", request.getRequestId());

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
