package com.silenteight.hsbc.bridge.bulk.repository;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.dto.GetStatusQueryResult;
import com.silenteight.hsbc.bridge.bulk.dto.UpdateBulkResult;
import com.silenteight.hsbc.bridge.bulk.exception.BulkAlreadyCompletedException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.BulkProcessingResult;
import com.silenteight.hsbc.bridge.bulk.BulkStatus;
import com.silenteight.hsbc.bridge.bulk.dto.BulkItem;
import com.silenteight.hsbc.bridge.bulk.dto.CreateBulkResult;
import com.silenteight.hsbc.bridge.bulk.query.GetResultQuery;
import com.silenteight.hsbc.bridge.bulk.query.GetStatusQuery;
import com.silenteight.hsbc.bridge.rest.model.input.Alerts;
import com.silenteight.hsbc.bridge.rest.model.input.SolvedAlert;
import com.silenteight.hsbc.bridge.rest.model.input.SolvedAlertStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class InMemoryBulkRepository implements BulkQueryRepository, BulkWriteRepository {

  private final Map<UUID, List<BulkItem>> bulkStore = new HashMap<>();

  private final Map<UUID, Integer> counterForMock = new HashMap<>();

  @Override
  public CreateBulkResult createBulk(Alerts alerts) {
    var uuid = UUID.randomUUID();
    var bulkItems = getBulkItems(alerts);

    bulkStore.put(uuid, bulkItems);

    //TODO only demo
    counterForMock.put(uuid, 0);

    log.info("{}", bulkStore);

    return CreateBulkResult.builder()
        .bulkId(uuid)
        .bulkItems(bulkItems)
        .build();
  }

  private List<BulkItem> getBulkItems(Alerts alerts) {
    return alerts.stream()
        .map(r -> new BulkItem(
            r.getSystemInformation().getCasesWithAlertURL().getID(),
            BulkStatus.STORED))
        .collect(Collectors.toList());
  }

  @Override
  public UpdateBulkResult updateBulkStatus(UUID id, BulkStatus status) {
    var items = read(id);

    if (status == BulkStatus.CANCELLED && getStatus(items) == BulkStatus.COMPLETED) {
      throw new BulkAlreadyCompletedException("Bulk: " + id + " is already completed, can't be cancelled!");
    }

    items.forEach(t -> t.setStatus(status));

    log.info("{}", bulkStore);

    return UpdateBulkResult.builder()
        .bulkId(id)
        .bulkStatus(status)
        .bulkItems(items)
        .build();
  }

  @Override
  public GetStatusQueryResult getStatus(GetStatusQuery query) {
    var id = query.getBulkId();
    var items = read(id);

    simulateStatusChangeForDemo(query, id);

    return GetStatusQueryResult.builder()
        .bulkStatus(getStatus(items))
        .bulkItems(items)
        .build();
  }

  //TODO only demo
  private void simulateStatusChangeForDemo(GetStatusQuery query, UUID id) {
    var currentCounter = counterForMock.get(id);
    if (currentCounter == 2) {
      updateBulkStatus(id, BulkStatus.PROCESSING);
    } else if (currentCounter >= 5) {
      updateBulkStatus(id, BulkStatus.COMPLETED);
    }
    counterForMock.replace(query.getBulkId(), currentCounter +1);
  }

  @Override
  public BulkProcessingResult getResult(GetResultQuery query) {
    var id = query.getBulkId();
    var items = read(id);
    var solvedAlerts = items.stream().map(t-> {
      var solvedAlert = new SolvedAlert();
      solvedAlert.setId(t.getId());
      solvedAlert.setComment("S8 recommended action: False Positive");
      solvedAlert.setRecommendation(SolvedAlertStatus.FALSE_POSITIVE);
      return solvedAlert;
    }).collect(Collectors.toList());
    return new BulkProcessingResult(id, getStatus(items), solvedAlerts);
  }

  private List<BulkItem> read(UUID id) {
    if (!bulkStore.containsKey(id)) {
      throw new BulkIdNotFoundException("BulkID: " + id + " does not exist!");
    }

    return bulkStore.get(id);
  }

  private BulkStatus getStatus(List<BulkItem> items) {
    return items.get(0).getStatus();
  }
}
