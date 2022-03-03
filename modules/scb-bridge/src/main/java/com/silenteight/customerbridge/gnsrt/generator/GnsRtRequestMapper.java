package com.silenteight.customerbridge.gnsrt.generator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;
import com.silenteight.customerbridge.common.gnsparty.GnsParty;
import com.silenteight.customerbridge.common.gnsparty.RecordParser;
import com.silenteight.customerbridge.common.hitdetails.HitDetailsParser;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.common.hitdetails.model.Suspect;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtRecommendationRequest;

import java.util.Collection;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class GnsRtRequestMapper {

  private final HitDetailsParser hitDetailsParser;

  GnsRtRecommendationRequest map(@NonNull List<AlertRecord> alertRecords) {
    checkArgument(!alertRecords.isEmpty());

    return buildRecordToGnsRequestMapper(alertRecords).toGnsRtRequest();
  }

  private RecordToGnsRequestMapper buildRecordToGnsRequestMapper(List<AlertRecord> alertRecords) {
    AlertRecord alertRecord = alertRecords.get(0);
    return RecordToGnsRequestMapper
        .builder()
        .record(alertRecord)
        .alertedParty(parseGnsParty(alertRecord))
        .alerts(alertRecords.stream().map(this::toAlertDto).collect(toList()))
        .build();
  }

  private AlertDto toAlertDto(AlertRecord alertRecord) {
    return AlertDto.builder()
        .details(alertRecord.getDetails())
        .suspects(parseHitDetails(alertRecord.getDetails()))
        .systemId(alertRecord.getSystemId())
        .unit(alertRecord.getUnit())
        .build();
  }

  private Collection<Suspect> parseHitDetails(String details) {
    HitDetails hitDetails = hitDetailsParser.parse(details);
    return hitDetails.extractUniqueSuspects();
  }

  private static GnsParty parseGnsParty(AlertRecord alertRecord) {
    return RecordParser.parse(
        alertRecord.getSystemId(),
        alertRecord.getCharSep(),
        alertRecord.getFmtName(),
        alertRecord.getRecord()
    );
  }
}
