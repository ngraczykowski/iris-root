package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.*;
import com.silenteight.payments.bridge.etl.firco.parser.MessageFormat;
import com.silenteight.payments.bridge.etl.firco.parser.MessageParserFacade;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.model.InvalidMessageException;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.*;
import com.silenteight.payments.bridge.svb.oldetl.service.shitcode.*;
import com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure.NAMEADDRESS_FORMAT_F;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService implements ExtractAlertEtlResponseUseCase {

  private static final List<String> FORMATS = List.of("INT", "FED", "IAT-I", "IAT-O", "O-F");

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
            alertMessageDto.getApplicationCode(), messageData, hit,
            MessageFieldStructure.UNSTRUCTURED, idx));
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
      String applicationCode, MessageData messageData, HitDto hit,
      MessageFieldStructure messageFieldStructure, int index) {

    var alertedPartyData =
        extractAlertedPartyData(
            messageData, hit.getTag(), messageFieldStructure,
            extractMessageFormat(applicationCode, messageData));

    var accountNumberOrNormalizedName =
        alertedPartyData.getAccountNumber() == null ? alertedPartyData.getNames().get(0)
                                                    : alertedPartyData.getAccountNumber();

    var hitAndWatchlistPartyData = extractHitAndWatchlistPartyData(
        buildTransactionMessage(applicationCode, messageData), hit, accountNumberOrNormalizedName);

    return new HitData(hit.getMatchId(index), alertedPartyData, hitAndWatchlistPartyData);
  }

  public static AlertedPartyData extractAlertedPartyData(
      MessageData messageData, String hitTag,
      MessageFieldStructure messageFieldStructure,
      String messageFormat) {

    boolean tagValueInFormatF = ifTagValueInFormatF(messageData.getLines(hitTag));
    switch (hitTag) {
      case "ORIGINATOR":
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractOriginatorAlertedPartyData(messageData).extract(
              messageFieldStructure, messageFormat);
        }
      case "BENE":
        if (tagValueInFormatF) {
          return new ExtractOriginatorBeneFormatFAlertedPartyData(messageData, hitTag).extract(
              NAMEADDRESS_FORMAT_F);
        } else {
          return new ExtractBeneOrgbankInsbankAlertedPartyData(
              messageData, hitTag, messageFormat).extract(messageFieldStructure);
        }
      case "ORGBANK":
      case "INSBANK":
        return new ExtractBeneOrgbankInsbankAlertedPartyData(
            messageData, hitTag, messageFormat).extract(messageFieldStructure);
      case "50F":
        return new Extract50FAlertedPartyData(messageData, hitTag).extract(messageFieldStructure);
      case "RECEIVBANK":
        return new ExtractReceivbankAlertedPartyData(
            messageData, hitTag, messageFormat).extract(messageFieldStructure);
      case "50K":
      case "59":
      case "50":
        return new Extract50k59AlertedPartyData(messageData).extract(hitTag, messageFieldStructure);
      default:
        throw new InvalidMessageException("Tag not supported " + hitTag);
    }
  }

  private TransactionMessage buildTransactionMessage(
      String applicationCode, MessageData messageData) {

    switch (applicationCode) {
      case "GFX":
        return new GfxTransactionMessage(messageData);
      case "PEP":
        return new PepTransactionMessage(messageData);
      case "GTEX":
        return new GtexTransactionMessage(messageData);
      default:
        throw new InvalidMessageException("Application not supported " + applicationCode);
    }
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      TransactionMessage transactionMessage, HitDto hit, String accountNumberOrNormalizedName) {

    var hittedEntity = hit.getHittedEntity();
    var synonymIndex = CommonUtils.toPositiveInt(hit.getSynonymIndex(), 0);
    var extractedName = HitNameExtractor.extractName(synonymIndex, hittedEntity);

    var tag = hit.getTag();
    var allMatchingTexts = transactionMessage.getAllMatchingTexts(tag, hit.getMatchingText());
    var fieldValue = transactionMessage.getHitTagValue(tag);
    var allMatchingFieldValues = transactionMessage.getAllMatchingTagValues(
        tag, hit.getMatchingText());

    List<CodeDto> codes = Optional.of(hit)
        .map(HitDto::getHittedEntity)
        .map(HittedEntityDto::getCodes)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(HittedEntityCodeDto::getCode)
        .collect(Collectors.toList());

    List<String> searchCodes = codes.stream()
        .filter(codeDto -> "SearchCode".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());

    List<String> passports = codes.stream()
        .filter(codeDto -> "Passport".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());

    List<String> natIds = codes.stream()
        .filter(codeDto -> "NationalID".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());

    List<String> bics = codes.stream()
        .filter(codeDto -> "Bic".equals(codeDto.getType()))
        .map(CodeDto::getName)
        .collect(Collectors.toList());

    return HitAndWatchlistPartyData.builder()
        .solutionType(SolutionType.ofCode(hit.getSolutionType()))
        .watchlistType(WatchlistType.ofCode(hittedEntity.getType()))
        .tag(tag)
        .id(hittedEntity.getId())
        .name(extractedName)
        .entityText(hit.getEntityText())
        .matchingText(hit.getMatchingText())
        .allMatchingTexts(allMatchingTexts)
        .allMatchingFieldValues(allMatchingFieldValues)
        .fieldValue(fieldValue)
        .accountNumber(accountNumberOrNormalizedName)
        .postalAddresses(
            LocationExtractorHelper.extractPostalAddresses(hittedEntity.getAddresses()))
        .cities(LocationExtractorHelper.extractListOfCities(hittedEntity.getAddresses()))
        .states(LocationExtractorHelper.extractListOfStates(hittedEntity.getAddresses()))
        .countries(LocationExtractorHelper.extractListOfCountries(hittedEntity.getAddresses()))
        .mainAddress(LocationExtractorHelper.extractIsMainAddress(hittedEntity.getAddresses()))
        .origin((hittedEntity.getOrigin()))
        .designation((hittedEntity.getDesignation()))
        .searchCodes(searchCodes)
        .passports(passports)
        .natIds(natIds)
        .bics(bics)
        .build();
  }

  private static boolean ifTagValueInFormatF(List<String> tagValueLines) {
    List<String> formatFPrefixes = List.of("2/", "3/");
    long numberOfLinesContainsFormatFPrefixes =
        tagValueLines
            .stream()
            .filter(element -> formatFPrefixes.contains(element.substring(0, 2)))
            .count();
    return numberOfLinesContainsFormatFPrefixes >= 2;
  }

  private static String extractMessageFormat(String applicationCode, MessageData messageData) {
    if (applicationCode.equals("GTEX"))
      return "SWF";

    var type = messageData.getValue("TYPE");

    for (var format : FORMATS) {
      if (type.contains(format))
        return format;
    }

    if (type.contains("BOO"))
      return "IAT-O";

    throw new InvalidMessageException("Couldn't map cmapi to FKCO_V_FORMAT");
  }
}
