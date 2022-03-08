package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty;
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.RecordParser;
import com.silenteight.scb.ingest.adapter.incomming.common.indicator.RecordSignCreator;

@RequiredArgsConstructor
public class BaseAlertCompositeRowProcessor {

  private final DateConverter dateConverter;

  protected RecordToAlertMapper createRecordToAlertMapper(
      AlertRecord alertRow,
      DecisionsCollection decisions,
      boolean watchlistLevel) {

    return RecordToAlertMapper
        .builder()
        .alertData(alertRow)
        .decisionsCollection(decisions)
        .filtered(dateConverter.convert(
            alertRow.getFilteredString())
            .orElse(null))
        .alertedParty(makeGnsParty(alertRow))
        .recordSignature(RecordSignCreator.fromRecord(alertRow.getRecord(), alertRow.getCharSep()))
        .watchlistLevel(watchlistLevel)
        .requestRecommendation(true)
        .build();
  }

  private static GnsParty makeGnsParty(AlertRecord alertRow) {
    return RecordParser.parse(
        alertRow.getSystemId(),
        alertRow.getCharSep(),
        alertRow.getFmtName(),
        alertRow.getRecord()
    );
  }
}
