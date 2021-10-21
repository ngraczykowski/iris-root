package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.*;
import com.silenteight.payments.bridge.etl.firco.parser.MessageFormat;
import com.silenteight.payments.bridge.etl.firco.parser.MessageParserFacade;
import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.*;
import com.silenteight.payments.bridge.svb.oldetl.service.shitcode.*;
import com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService implements ExtractAlertEtlResponseUseCase {

  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;

  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto) {
    if (log.isDebugEnabled()) {
      log.debug("Parsing alert for ETL: systemId={}", alertMessageDto.getSystemID());
    }

    var hits = new ArrayList<HitData>();
    var messageData = new MessageParserFacade().parse(
        MessageFormat.valueOf(alertMessageDto.getMessageFormat()),
        alertMessageDto.getMessageData());

    for (var requestHitDto : alertMessageDto.getHits()) {
      var hit = requestHitDto.getHit();

      var awefulMessageStructure = extractMessageStructureUseCase.extractMessageStructure(
          ExtractAlertedPartyDataRequest.builder()
              .messageData(alertMessageDto.getMessageData())
              .applicationCode(alertMessageDto.getApplicationCode())
              .messageType(alertMessageDto.getMessageType())
              .matchingText(hit.getMatchingText())
              .tag(hit.getTag())
              .build());

      var hitData = createHitData(
          alertMessageDto.getApplicationCode(), messageData, hit,
          awefulMessageStructure.getMessageFieldStructure());

      hits.add(hitData);
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
        .hits(hits)
        .build();
  }

  private HitData createHitData(
      String applicationCode, MessageData messageData, HitDto hit,
      MessageFieldStructure messageFieldStructure) {

    var alertedPartyData = extractAlertedPartyData(
        applicationCode, messageData, hit.getTag(), messageFieldStructure);

    var hitAndWatchlistPartyData = extractHitAndWatchlistPartyData(
        buildTransactionMessage(applicationCode, messageData), hit);

    return new HitData(alertedPartyData, hitAndWatchlistPartyData);
  }

  private AlertedPartyData extractAlertedPartyData(
      String applicationCode, MessageData messageData, String tag,
      MessageFieldStructure messageFieldStructure) {

    switch (applicationCode) {
      case "GFX":
        return new ExtractGfxAlertedPartyData(messageData, tag).extract(messageFieldStructure);
      case "PEP":
        return new ExtractPepAlertedPartyData(messageData).extract();
      case "GTEX":
        return new ExtractGtexAlertedPartyData(messageData).extract();
      default:
        throw new IllegalArgumentException("Application not supported " + applicationCode);
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
        throw new IllegalArgumentException("Application not supported " + applicationCode);
    }
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      TransactionMessage transactionMessage, HitDto hit) {

    var hittedEntity = hit.getHittedEntity();
    var synonymIndex = CommonUtils.toPositiveInt(hit.getSynonymIndex(), 0);
    var extractedName = HitNameExtractor.extractName(synonymIndex, hittedEntity);

    var tag = hit.getTag();
    var allMatchingTexts = transactionMessage.getAllMatchingTexts(tag);
    var fieldValue = transactionMessage.getHitTagValue(tag);
    var allMatchingFieldValues = transactionMessage.getAllMatchingTagValues(
        tag, hit.getMatchingText());
    var accountNumberOrNormalizedName = transactionMessage.getAccountNumber(tag);

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
        .accountNumberOrNormalizedName(accountNumberOrNormalizedName.orElse("no_data"))
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
}
