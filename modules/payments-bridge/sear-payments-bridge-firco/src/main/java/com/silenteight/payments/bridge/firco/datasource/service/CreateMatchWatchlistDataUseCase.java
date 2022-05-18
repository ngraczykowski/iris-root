package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.silenteight.payments.bridge.firco.datasource.service.CreateAlertEtlResponseUseCase.extractHitAndWatchlistPartyData;

@Service
@Slf4j
@RequiredArgsConstructor
class CreateMatchWatchlistDataUseCase {

  private final MessageParserUseCase messageParserUseCase;

  public Map<String, HitAndWatchlistPartyData> getWatchlistDataForMatch(
      AlertMessageDto alertMessageDto) {

    var hitData = new HashMap<String, HitAndWatchlistPartyData>();
    var skippedHits = new ArrayList<String>();
    var messageData = getMessageData(alertMessageDto);

    var alertMessageDtoHits = alertMessageDto.getHits();
    // XXX(ahaczewski): WATCH OUT! RegisterAlertEndpoint#getMatchIds() assumes the same iteration
    //  order!!! Make sure you keep it in sync, until shit gets cleaned!!!
    for (int idx = 0, hitCount = alertMessageDtoHits.size(); idx < hitCount; idx++) {
      var hit = alertMessageDtoHits.get(idx).getHit();
      if (hit.isBlocking()) {
        hitData.put(hit.getMatchId(idx), extractHitAndWatchlistPartyData(
            messageData,
            hit));
      } else {
        skippedHits.add(hit.getMatchId(idx));
      }
    }

    if (!skippedHits.isEmpty()) {
      log.info("Skipping non-blocking hits: systemId={}, hits={}",
          alertMessageDto.getSystemID(), skippedHits);
    }

    return hitData;
  }


  private MessageData getMessageData(AlertMessageDto alertMessageDto) {
    return messageParserUseCase.parse(
        MessageFormat.valueOf(alertMessageDto.getMessageFormat()),
        alertMessageDto.getMessageData());
  }
}
