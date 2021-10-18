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
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;
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
  private final ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;
  private final FieldValueExtractor fieldValueExtractor;
  private final ExtractMatchTextUseCase extractMatchTextUseCase;

  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto) {
    if (log.isDebugEnabled()) {
      log.debug("Parsing alert for ELT: systemId={}", alertMessageDto.getSystemID());
    }

    List<HitData> hits = new ArrayList<>();

    for (RequestHitDto requestHitDto : alertMessageDto.getHits()) {
      hits.add(createHitData(alertMessageDto, requestHitDto));
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

  private HitData createHitData(AlertMessageDto alertMessageDto, RequestHitDto requestHitDto) {
    ExtractAlertedPartyDataRequest alertedPartyDataRequest =
        createExtractAlertedPartyDataRequest(alertMessageDto, requestHitDto);
    AbstractMessageStructure messageStructure =
        extractMessageStructureUseCase.extractMessageStructure(alertedPartyDataRequest);
    AlertedPartyData alertedPartyData =
        extractAlertedPartyDataUseCase.extractAlertedPartyData(alertedPartyDataRequest);
    HitAndWatchlistPartyData hitAndWatchlistPartyData =
        extractHitAndWatchlistPartyData(alertMessageDto, requestHitDto, messageStructure);
    return new HitData(alertedPartyData, hitAndWatchlistPartyData);
  }

  private static ExtractAlertedPartyDataRequest createExtractAlertedPartyDataRequest(
      AlertMessageDto alertMessageDto, RequestHitDto requestHitDto) {
    return ExtractAlertedPartyDataRequest
        .builder()
        .messageData(alertMessageDto.getMessageData())
        .messageType(alertMessageDto.getMessageType())
        .matchingText(requestHitDto.getHit().getMatchingText())
        .tag(requestHitDto.getHit().getTag())
        .applicationCode(alertMessageDto.getApplicationCode())
        .build();
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
      AlertMessageDto alertMessageDto,
      RequestHitDto requestHitDto, AbstractMessageStructure messageStructure) {

    int synonymIndex = CommonUtils.toPositiveInt(requestHitDto.getHit().getSynonymIndex(), 0);
    String extractedName = HitNameExtractor.extractName(requestHitDto, synonymIndex);
    List<String> allMatchingTexts = extractMatchTextUseCase.extractAllMatchingTexts(
        messageStructure, requestHitDto.getHit().getMatchingText());
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
            .tag(requestHitDto.getHit().getTag())
            .matchingFields(allMatchingFieldValues)
            .build());

    List<CodeDto> codes = Optional.of(requestHitDto.getHit())
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
        .solutionType(SolutionType.ofCode(requestHitDto.getHit().getSolutionType()))
        .watchlistType(WatchlistType.ofCode(requestHitDto.getHit().getHittedEntity().getType()))
        .tag(requestHitDto.getHit().getTag())
        .id(requestHitDto.getHit().getHittedEntity().getId())
        .name(extractedName)
        .entityText(requestHitDto.getHit().getEntityText())
        .matchingText(requestHitDto.getHit().getMatchingText())
        .allMatchingTexts(allMatchingTexts)
        .allMatchingFieldValues(allMatchingFieldValues)
        .fieldValue(fieldValue)
        .accountNumberOrNormalizedName(accountNumberOrNormalizedName.orElse("no_data"))
        .postalAddresses(LocationExtractorHelper.extractPostalAddresses(requestHitDto))
        .cities(LocationExtractorHelper.extractListOfCities(requestHitDto))
        .states(LocationExtractorHelper.extractListOfStates(requestHitDto))
        .countries(LocationExtractorHelper.extractListOfCountries(requestHitDto))
        .mainAddress(LocationExtractorHelper.extractIsMainAddress(requestHitDto))
        .origin((requestHitDto.getHit().getHittedEntity().getOrigin()))
        .designation((requestHitDto.getHit().getHittedEntity().getDesignation()))
        .searchCodes(searchCodes)
        .passports(passports)
        .natIds(natIds)
        .bics(bics)
        .build();
  }
}
