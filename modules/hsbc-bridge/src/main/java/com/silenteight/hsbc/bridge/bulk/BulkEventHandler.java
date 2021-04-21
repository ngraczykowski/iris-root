package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.adjudication.AdjudicateFailedEvent;
import com.silenteight.hsbc.bridge.analysis.dto.AnalysisDto;
import com.silenteight.hsbc.bridge.bulk.event.BulkProcessingStartedEvent;

import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Slf4j
class BulkEventHandler {

  private final BulkRepository bulkRepository;

  @EventListener
  @Transactional
  public void onAdjudicateFailedEvent(AdjudicateFailedEvent event) {
    var bulk = bulkRepository.findById(event.getBulkId());
    updateBulkWithError(bulk, event.getErrorMessage());

    log.debug("AdjudicateFailedEvent handled successfully, bulkId =  {}", event.getBulkId());
  }

  @EventListener
  @Transactional
  public void onBulkProcessingStartedEvent(BulkProcessingStartedEvent event) {
    var bulk = bulkRepository.findById(event.getBulkId());
    updateBulkWithAnalysis(bulk, event.getAnalysis());

    log.debug("BulkProcessingStartedEvent handled successfully, bulkId =  {}", event.getBulkId());
  }

  private void updateBulkWithAnalysis(Bulk bulk, AnalysisDto analysis) {
    bulk.setPolicyName(analysis.getPolicy());
    bulkRepository.save(bulk);
  }

  private void updateBulkWithError(Bulk bulk, String errorMessage) {
    bulk.setErrorMessage(errorMessage);
    bulk.setStatus(BulkStatus.ERROR);
    bulk.setErrorTimestamp(OffsetDateTime.now());
    bulkRepository.save(bulk);
  }
}
