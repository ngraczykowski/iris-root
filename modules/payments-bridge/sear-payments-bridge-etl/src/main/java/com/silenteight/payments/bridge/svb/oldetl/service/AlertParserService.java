package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.*;
import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.model.GetAccountNumberRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertEtlResponse;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;
import com.silenteight.payments.bridge.svb.oldetl.util.CommonUtils;
import com.silenteight.sep.base.aspects.logging.LogContext;

import org.slf4j.MDC;
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
  private final ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;
  private final FieldValueExtractor fieldValueExtractor;
  private final ExtractMatchTextUseCase extractMatchTextUseCase;

  @LogContext
  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto dto) {
    MDC.put("systemId", dto.getSystemID());
    MDC.put("messageId", dto.getMessageID());

    if (log.isDebugEnabled()) {
      log.debug("Parsing message: systemId={}, messageId={}",
          dto.getSystemID(), dto.getMessageID());
    }

    List<HitData> hits = new ArrayList<>();

    for (RequestHitDto requestHitDto : dto.getHits()) {
      hits.add(createHitData(dto, requestHitDto));
    }

    return AlertEtlResponse.builder()
        .systemId(dto.getSystemID())
        .messageId(dto.getMessageID())
        .messageType(dto.getMessageType())
        .applicationCode(dto.getApplicationCode())
        .messageFormat(dto.getMessageFormat())
        .messageData(dto.getMessageData())
        .businessUnit(dto.getBusinessUnit())
        .unit(dto.getUnit())
        .senderReference(dto.getSenderReference())
        .ioIndicator(dto.getIoIndicator())
        //TODO check why AlertStatusExtractor is no longer needed
        .currentStatus(dto.getCurrentStatus())
        .hits(hits)
        .build();
  }

  private HitData createHitData(AlertMessageDto alert, RequestHitDto hit) {
    var alertedPartyDataRequest =
        createExtractAlertedPartyDataRequest(alert, hit);
    var messageStructure =
        extractMessageStructureUseCase.extractMessageStructure(alertedPartyDataRequest);
    var alertedPartyData =
        extractAlertedPartyDataUseCase.extractAlertedPartyData(alertedPartyDataRequest);
    var hitAndWatchlistPartyData =
        extractHitAndWatchlistPartyData(alert, hit, messageStructure);

    return new HitData(alertedPartyData, hitAndWatchlistPartyData);
  }

  private static ExtractAlertedPartyDataRequest createExtractAlertedPartyDataRequest(
      AlertMessageDto alert, RequestHitDto hit) {
    return ExtractAlertedPartyDataRequest
        .builder()
        .messageData(alert.getMessageData())
        .messageType(alert.getMessageType())
        .matchingText(hit.getHit().getMatchingText())
        .tag(hit.getHit().getTag())
        .applicationCode(alert.getApplicationCode())
        .build();
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      AlertMessageDto alert,
      RequestHitDto hit, AbstractMessageStructure messageStructure) {

    int synonymIndex = CommonUtils.toPositiveInt(hit.getHit().getSynonymIndex(), 0);
    String extractedName = HitNameExtractor.extractName(hit, synonymIndex);

    List<String> allMatchingTexts = extractMatchTextUseCase.extractAllMatchingTexts(
        messageStructure, hit.getHit().getMatchingText());
    String fieldValue = fieldValueExtractor
        .extractFieldValues(messageStructure)
        .get(0)
        .stream()
        .findFirst()
        .orElse(null);
    List<String> allMatchingFieldValues = fieldValueExtractor
        .extractFieldValues(messageStructure)
        .stream()
        .findFirst()
        .orElse(Collections.emptyList());

    Optional<String> accountNumberOrNormalizedName = messageStructure.getAccountNumber(
        GetAccountNumberRequest
            .builder()
            .applicationCode(alert.getApplicationCode())
            .tag(hit.getHit().getTag())
            .message(messageStructure.getMessageData())
            .matchingFields(allMatchingFieldValues)
            .build());

    List<CodeDto> codes = Optional.of(hit.getHit())
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
        .messageStructure(messageStructure)
        .solutionType(SolutionType.ofCode(hit.getHit().getSolutionType()))
        .watchlistType(WatchlistType.ofCode(hit.getHit().getHittedEntity().getType()))
        .tag(hit.getHit().getTag())
        .id(hit.getHit().getHittedEntity().getId())
        .name(extractedName)
        .entityText(hit.getHit().getEntityText())
        .matchingText(hit.getHit().getMatchingText())
        .allMatchingTexts(allMatchingTexts)
        .allMatchingFieldValues(allMatchingFieldValues)
        .fieldValue(fieldValue)
        .accountNumberOrNormalizedName(accountNumberOrNormalizedName.orElse("no_data"))
        .postalAddresses(LocationExtractorHelper.extractPostalAddresses(hit))
        .cities(LocationExtractorHelper.extractListOfCities(hit))
        .states(LocationExtractorHelper.extractListOfStates(hit))
        .countries(LocationExtractorHelper.extractListOfCountries(hit))
        .mainAddress(LocationExtractorHelper.extractIsMainAddress(hit))
        .origin((hit.getHit().getHittedEntity().getOrigin()))
        .designation((hit.getHit().getHittedEntity().getDesignation()))
        .searchCodes(searchCodes)
        .passports(passports)
        .natIds(natIds)
        .bics(bics)
        .build();
  }
}
