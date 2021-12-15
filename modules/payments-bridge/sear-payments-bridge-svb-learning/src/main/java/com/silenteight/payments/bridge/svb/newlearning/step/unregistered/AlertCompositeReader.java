package com.silenteight.payments.bridge.svb.newlearning.step.unregistered;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.svb.newlearning.domain.AlertComposite;
import com.silenteight.sep.base.common.batch.reader.BetterJdbcCursorItemReader;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.support.AbstractItemStreamItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.QueryTimeoutException;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
class AlertCompositeReader extends AbstractItemStreamItemReader<AlertComposite> implements
    InitializingBean {

  private final BetterJdbcCursorItemReader<Long> alertIdsReader;
  private final int pageSize;

  private int currentAlertIndex = 0;
  private List<AlertComposite> alertComposites;

  AlertCompositeReader(BetterJdbcCursorItemReader<Long> alertIdsReader, int pageSize) {
    this.alertIdsReader = alertIdsReader;
    this.pageSize = pageSize;

    log.debug("Initializing AlertCompositeReader with page size = {}", pageSize);

    alertComposites = new ArrayList<>(pageSize);
  }

  @Override
  public AlertComposite read() throws Exception {

    if (currentAlertIndex >= alertComposites.size())
      alertComposites = readAlertComposites();

    return currentAlertIndex < alertComposites.size() ? alertComposites.get(currentAlertIndex++)
                                                      : null;
  }

  private List<AlertComposite> readAlertComposites() throws Exception {
    var ids = fetchAlertIds();

    log.info("Fetched alert Ids: count = {}", ids.size());

    currentAlertIndex = 0;
    return ids
        .stream()
        .map(id -> AlertComposite.builder().alertId(id).build())
        .collect(toList()); // Temporary solution before introducing composite fetcher.
  }

  private List<Long> fetchAlertIds() throws Exception {
    log.trace("Fetching alert ids");

    List<Long> ids = new ArrayList<>(pageSize);

    for (int i = 0; i < pageSize; i++) {
      Long id = alertIdsReader.read();

      if (id == null)
        break;

      ids.add(id);
    }

    log.trace("Fetched alert ids = {}", ids);

    return ids;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    alertIdsReader.afterPropertiesSet();
  }

  @Override
  public void open(ExecutionContext executionContext) {
    try {
      alertIdsReader.open(executionContext);
    } catch (QueryTimeoutException e) {
      throw new ItemStreamException(
          "Query to the Database timed out! Consider increasing the query timeout setting.",
          e.getCause());
    } catch (Exception e) {
      throw new ItemStreamException(
          "Unexpected exception occurred during reading opening connection to db.",
          e.getCause());
    }
  }

  @Override
  public void update(ExecutionContext executionContext) {
    alertIdsReader.update(executionContext);
  }

  @Override
  public void close() {
    alertIdsReader.close();
  }
}
