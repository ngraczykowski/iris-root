package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;
import com.silenteight.scb.ingest.adapter.incomming.cbs.gateway.CbsHitDetailsHelperFetcher;
import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser.ParserException;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails;
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect;

import com.google.common.base.Preconditions;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import static io.micrometer.core.instrument.util.StringUtils.isBlank;
import static java.util.Collections.emptyList;

@Slf4j
@RequiredArgsConstructor
public class SuspectDataFetcher {

  private final HitDetailsParser hitDetailsParser;
  private final CbsHitDetailsHelperFetcher cbsHitDetailsHelperFetcher;

  public SuspectsCollection parseHitDetails(Connection connection, @NonNull AlertRecord alertRow) {
    Preconditions.checkNotNull(alertRow.getBatchId(), "Batch Id cannot be null");
    String systemId = alertRow.getSystemId();
    String batchId = alertRow.getBatchId();
    String details = alertRow.getDetails();

    if (log.isDebugEnabled())
      log.debug("Parsing hit details for an alert: systemId={}, batchId={}", systemId, batchId);

    if (isBlank(details)) {
      log.warn("An alert has no hit details");
      return createSuspectsCollection(emptyList());
    }

    try {
      HitDetails hitDetails = hitDetailsParser.parse(details);
      fillSuspectData(connection, systemId, batchId, hitDetails);

      return createSuspectsCollection(hitDetails.extractUniqueSuspects());
    } catch (ParserException e) {
      log.error("Cannot parse hit details for an alert: systemId={}", systemId, e);
      return createSuspectsCollection(emptyList());
    }
  }

  private static SuspectsCollection createSuspectsCollection(Collection<Suspect> suspects) {
    return new SuspectsCollection(suspects);
  }

  private void fillSuspectData(
      Connection connection, String systemId, String batchId, HitDetails hitDetails) {
    List<CbsHitDetails> cbsHitDetails =
        cbsHitDetailsHelperFetcher.fetch(connection, systemId, batchId);

    for (Suspect suspect : hitDetails.getSuspects()) {
      if (!cbsHitDetails.isEmpty()) {
        suspect.loadSuspectWithNeoFlag(cbsHitDetails);
      }
    }
  }
}
