package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.HitDto;
import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.firco.datasource.model.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.STRUCTURED;
import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.ofTag;
import static com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService.extractAlertedPartyData;
import static com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService.extractFircoFormat;
import static com.silenteight.payments.bridge.svb.oldetl.service.impl.TransactionMessage.getAllMatchingTexts;

@Service
@Slf4j
@RequiredArgsConstructor
class CreateAlertEtlResponseUseCase {

  private final MessageParserUseCase messageParserUseCase;

  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto) {
    if (log.isDebugEnabled()) {
      log.debug("Parsing alert for ETL: systemId={}", alertMessageDto.getSystemID());
    }

    var hitData = new ArrayList<HitData>();
    var skippedHits = new ArrayList<String>();
    var messageData = getMessageData(alertMessageDto);

    var alertMessageDtoHits = alertMessageDto.getHits();
    // XXX(ahaczewski): WATCH OUT! RegisterAlertEndpoint#getMatchIds() assumes the same iteration
    //  order!!! Make sure you keep it in sync, until shit gets cleaned!!!
    for (int idx = 0, hitCount = alertMessageDtoHits.size(); idx < hitCount; idx++) {
      var hit = alertMessageDtoHits.get(idx).getHit();

      if (hit.isNotBlocking() || !ofTag(hit.getTag()).equals(STRUCTURED)) {
        skippedHits.add(hit.getMatchId(idx));
        continue;
      }

      hitData.add(createHitData(
          alertMessageDto.getApplicationCode(), messageData, hit, idx));
    }

    if (!skippedHits.isEmpty()) {
      log.info("Skipping non-blocking hits: systemId={}, hits={}",
          alertMessageDto.getSystemID(), skippedHits);
    }

    return AlertEtlResponse.builder()
        .systemId(alertMessageDto.getSystemID())
        .messageId(alertMessageDto.getMessageID())
        .messageType(alertMessageDto.getMessageType())
        .applicationCode(alertMessageDto.getApplicationCode())
        .messageFormat(alertMessageDto.getMessageFormat())
        .messageData(alertMessageDto.getMessageData())
        .businessUnit(alertMessageDto.getBusinessUnit())
        .unit(alertMessageDto.getUnit())
        .senderReference(alertMessageDto.getSenderReference())
        .ioIndicator(alertMessageDto.getIoIndicator())
        .currentStatus(alertMessageDto.getCurrentStatus())
        .hits(hitData)
        .build();
  }

  private static HitData createHitData(
      String applicationCode, MessageData messageData, HitDto hit, int index) {

    var fircoFormat = extractFircoFormat(applicationCode, messageData);

    var alertedPartyData =
        extractAlertedPartyData(
            messageData,
            hit.getTag(),
            fircoFormat,
            applicationCode);

    var hitAndWatchlistPartyData = extractHitAndWatchlistPartyData(messageData, hit);

    return new HitData(hit.getMatchId(index), alertedPartyData, hitAndWatchlistPartyData);
  }

  public static HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      MessageData messageData, HitDto hit) {

    var hittedEntity = hit.getHittedEntity();

    var tag = hit.getTag();
    var allMatchingTexts = getAllMatchingTexts(messageData, tag, hit.getMatchingText());
    var fieldValue = messageData.getValue(tag);
    var allMatchingFieldValues = messageData.getAllMatchingTagValues(
        tag, hit.getMatchingText());

    return HitAndWatchlistPartyData.builder()
        .solutionType(SolutionType.ofCode(hit.getSolutionType()))
        .watchlistType(WatchlistType.ofCode(hittedEntity.getType()))
        .tag(tag)
        .id(hittedEntity.getId())
        .name(hit.extractWlName())
        .entityText(hit.getEntityText())
        .matchingText(hit.getMatchingText())
        .allMatchingTexts(allMatchingTexts)
        .allMatchingFieldValues(allMatchingFieldValues)
        .fieldValue(fieldValue)
        .postalAddresses(
            hittedEntity.findPostalAddresses())
        .cities(hittedEntity.findCities())
        .states(hittedEntity.findStates())
        .countries(hittedEntity.findCountries())
        .mainAddress(hittedEntity.findIsMainAddress())
        .origin((hittedEntity.getOrigin()))
        .designation((hittedEntity.getDesignation()))
        .searchCodes(hit.findSearchCodes())
        .passports(hit.findPassports())
        .natIds(hit.findNatIds())
        .bics(hit.findBics())
        .build();
  }

  private MessageData getMessageData(AlertMessageDto alertMessageDto) {
    return messageParserUseCase.parse(
        MessageFormat.valueOf(alertMessageDto.getMessageFormat()),
        alertMessageDto.getMessageData());
  }
}
