package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.HitDto;
import com.silenteight.payments.bridge.etl.firco.parser.MessageFormat;
import com.silenteight.payments.bridge.etl.firco.parser.MessageParserFacade;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.model.UnsupportedMessageException;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;
import com.silenteight.payments.bridge.svb.oldetl.service.impl.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.NAMEADDRESS_FORMAT_F;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService implements ExtractAlertEtlResponseUseCase {

  private static final List<String> SUPPORTED_FIRCO_FORMATS =
      List.of("INT", "FED", "IAT-I", "IAT-O", "O-F");

  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto) {
    if (log.isDebugEnabled()) {
      log.debug("Parsing alert for ETL: systemId={}", alertMessageDto.getSystemID());
    }

    var hitData = new ArrayList<HitData>();
    var skippedHits = new ArrayList<String>();
    var messageData = new MessageParserFacade().parse(
        MessageFormat.valueOf(alertMessageDto.getMessageFormat()),
        alertMessageDto.getMessageData());

    var alertMessageDtoHits = alertMessageDto.getHits();
    // XXX(ahaczewski): WATCH OUT! RegisterAlertEndpoint#getMatchIds() assumes the same iteration
    //  order!!! Make sure you keep it in sync, until shit gets cleaned!!!
    for (int idx = 0, hitCount = alertMessageDtoHits.size(); idx < hitCount; idx++) {
      var hit = alertMessageDtoHits.get(idx).getHit();
      if (hit.isBlocking()) {
        hitData.add(createHitData(
            alertMessageDto.getApplicationCode(), messageData, hit, idx));
      } else {
        skippedHits.add(hit.getMatchId(idx));
      }
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
        //TODO check why AlertStatusExtractor is no longer needed
        .currentStatus(alertMessageDto.getCurrentStatus())
        .hits(hitData)
        .build();
  }

  private HitData createHitData(
      String applicationCode, MessageData messageData, HitDto hit, int index) {

    var alertedPartyData =
        extractAlertedPartyData(
            messageData, hit.getTag(),
            extractFircoFormat(applicationCode, messageData));

    var hitAndWatchlistPartyData = extractHitAndWatchlistPartyData(
        new TransactionMessageImpl(messageData), hit);

    return new HitData(hit.getMatchId(index), alertedPartyData, hitAndWatchlistPartyData);
  }

  public static AlertedPartyData extractAlertedPartyData(
      MessageData messageData, String hitTag,
      String fircoFormat) {

    boolean tagValueInFormatF = ifTagValueInFormatF(messageData.getLines(hitTag));
    switch (hitTag) {
      case "ORIGINATOR":
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractOriginatorAlertedPartyData(messageData).extract(
              UNSTRUCTURED, fircoFormat);
        }
      case "BENE":
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractBeneOrgbankInsbankAlertedPartyData(
              messageData, hitTag, fircoFormat).extract(UNSTRUCTURED);
        }
      case "ORGBANK":
      case "INSBANK":
        return new ExtractBeneOrgbankInsbankAlertedPartyData(
            messageData, hitTag, fircoFormat).extract(UNSTRUCTURED);
      case "50F":
        return new Extract50FAlertedPartyData(messageData, hitTag).extract(UNSTRUCTURED);
      case "RECEIVBANK":
        return new ExtractReceivbankAlertedPartyData(
            messageData, hitTag, fircoFormat).extract(UNSTRUCTURED);
      case "50K":
      case "59":
      case "50":
        return new Extract50k59AlertedPartyData(messageData).extract(hitTag, UNSTRUCTURED);
      default:
        throw new UnsupportedMessageException("Tag not supported " + hitTag);
    }
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      TransactionMessage transactionMessage, HitDto hit) {

    var hittedEntity = hit.getHittedEntity();

    var tag = hit.getTag();
    var allMatchingTexts = transactionMessage.getAllMatchingTexts(tag, hit.getMatchingText());
    var fieldValue = transactionMessage.getHitTagValue(tag);
    var allMatchingFieldValues = transactionMessage.getAllMatchingTagValues(
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

  private static boolean ifTagValueInFormatF(List<String> tagValueLines) {
    var formatFPrefixes = List.of("2/", "3/");
    var numberOfLinesContainsFormatFPrefixes = tagValueLines.stream()
        .filter(line -> line.length() > 1)
        .filter(line -> formatFPrefixes.contains(
            line.substring(0, 2)))
        .count();

    return numberOfLinesContainsFormatFPrefixes >= 2;
  }

  private static String extractFircoFormat(String applicationCode, MessageData messageData) {
    if (applicationCode.equals("GTEX"))
      return "SWF";

    var type = messageData.getValue("TYPE");

    for (var format : SUPPORTED_FIRCO_FORMATS) {
      if (type.contains(format))
        return format;
    }

    if (type.contains("BOO"))
      return "IAT-O";

    throw new UnsupportedMessageException(
        "Unable to map unknown TYPE " + type + " to FKCO_V_FORMAT");
  }
}
