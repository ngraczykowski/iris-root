package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.HitDto;
import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.STRUCTURED;
import static com.silenteight.payments.bridge.common.dto.common.MessageStructure.ofTag;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.NAMEADDRESS_FORMAT_F;
import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.UNSTRUCTURED;
import static com.silenteight.payments.bridge.svb.oldetl.util.CommonTerms.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService implements ExtractAlertEtlResponseUseCase {

  private final MessageParserUseCase messageParserUseCase;
  private static final List<String> SUPPORTED_FIRCO_FORMATS =
      List.of(
          FIRCO_FORMAT_INT,
          FIRCO_FORMAT_FED,
          FIRCO_FORMAT_IAT_I,
          FIRCO_FORMAT_IAT_O,
          FIRCO_FORMAT_O_F);

  @Override
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
      if (hit.isBlocking()) {

        if (ofTag(hit.getTag()).equals(STRUCTURED)) {
          hitData.add(createHitData(
              alertMessageDto.getApplicationCode(), messageData, hit, idx));
        }

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

  @Override
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
            new TransactionMessageImpl(messageData),
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

  private static HitData createHitData(
      String applicationCode, MessageData messageData, HitDto hit, int index) {

    var alertedPartyData =
        extractAlertedPartyData(
            messageData, hit.getTag(),
            extractFircoFormat(applicationCode, messageData),
            applicationCode);

    var hitAndWatchlistPartyData = extractHitAndWatchlistPartyData(
        new TransactionMessageImpl(messageData), hit);

    return new HitData(hit.getMatchId(index), alertedPartyData, hitAndWatchlistPartyData);
  }

  public static AlertedPartyData extractAlertedPartyData(
      MessageData messageData, String hitTag,
      String fircoFormat, String applicationCode) {

    boolean tagValueInFormatF = ifTagValueInFormatF(messageData.getLines(hitTag));
    switch (hitTag) {
      case TAG_ORIGINATOR:
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractOriginatorAlertedPartyData(messageData).extract(
              UNSTRUCTURED, fircoFormat, applicationCode);
        }
      case TAG_BENE:
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractBeneOrgbankInsbankAlertedPartyData(
              messageData, hitTag, fircoFormat).extract(UNSTRUCTURED, applicationCode);
        }
      case TAG_ORGBANK:
      case TAG_INSBANK:
        return new ExtractBeneOrgbankInsbankAlertedPartyData(
            messageData, hitTag, fircoFormat).extract(UNSTRUCTURED, applicationCode);
      case TAG_50F:
        return new Extract50FAlertedPartyData(messageData, hitTag).extract(NAMEADDRESS_FORMAT_F);
      case TAG_RECEIVBANK:
        return new ExtractReceivbankAlertedPartyData(
            messageData, hitTag, fircoFormat).extract(UNSTRUCTURED);
      case TAG_50K:
      case TAG_59:
      case TAG_50:
        return new Extract50k59AlertedPartyData(messageData).extract(hitTag, UNSTRUCTURED);
      default:
        throw new UnsupportedMessageException("Tag not supported " + hitTag);
    }
  }

  private static HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
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
    var formatFPrefixes = List.of(ADDRESS_ROW_PREFIX, COUNTRY_ROW_PREFIX);
    var numberOfLinesContainsFormatFPrefixes = tagValueLines.stream()
        .filter(line -> line.length() > 1)
        .filter(line -> formatFPrefixes.contains(
            line.substring(0, 2)))
        .count();

    return numberOfLinesContainsFormatFPrefixes >= 2;
  }

  private static String extractFircoFormat(String applicationCode, MessageData messageData) {
    if (applicationCode.equals(APPLICATION_CODE_GTEX))
      return FIRCO_FORMAT_SWF;

    var type = messageData.getValue("TYPE");

    for (var format : SUPPORTED_FIRCO_FORMATS) {
      if (type.contains(format))
        return format;
    }

    if (type.contains("BOO"))
      return FIRCO_FORMAT_IAT_O;

    throw new UnsupportedMessageException(
        "Unable to map unknown TYPE " + type + " to FKCO_V_FORMAT");
  }
}
