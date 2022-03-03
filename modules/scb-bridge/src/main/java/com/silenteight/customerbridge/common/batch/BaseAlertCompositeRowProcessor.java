package com.silenteight.customerbridge.common.batch;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.gnsparty.GnsParty;
import com.silenteight.customerbridge.common.gnsparty.RecordParser;
import com.silenteight.customerbridge.common.indicator.RecordSignCreator;

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
