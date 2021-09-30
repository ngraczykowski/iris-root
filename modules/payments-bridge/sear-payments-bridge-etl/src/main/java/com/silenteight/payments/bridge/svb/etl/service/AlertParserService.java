package com.silenteight.payments.bridge.svb.etl.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.common.dto.common.SolutionType;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.common.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.common.dto.input.RequestHitDto;
import com.silenteight.payments.bridge.svb.etl.countrycode.CountryCodeExtractRequest;
import com.silenteight.payments.bridge.svb.etl.countrycode.CountryCodeExtractor;
import com.silenteight.payments.bridge.svb.etl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.etl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertEtlResponseUseCase;
import com.silenteight.payments.bridge.svb.etl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.etl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.etl.response.*;
import com.silenteight.payments.bridge.svb.etl.util.CommonUtils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class AlertParserService implements ExtractAlertEtlResponseUseCase {

  private final CountryCodeExtractor countryCodeExtractor;
  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;
  private final ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;

  public AlertEtlResponse createAlertEtlResponse(AlertMessageDto alertMessageDto) {
    log.debug("invoke");
    List<HitData> hits = new ArrayList<>();

    SourceSystem sourceSystem =
        AbstractMessageStructure.extractSourceSystem(alertMessageDto.getApplicationCode());

    for (RequestHitDto requestHitDto : alertMessageDto.getHits()) {
      hits.add(createHitData(sourceSystem, alertMessageDto, requestHitDto));
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
        .countryCode(extractCountryCode(sourceSystem, alertMessageDto))
        .hits(hits)
        .build();
  }

  private HitData createHitData(
      SourceSystem sourceSystem, AlertMessageDto alertMessageDto, RequestHitDto requestHitDto) {
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

  private ExtractAlertedPartyDataRequest createExtractAlertedPartyDataRequest(
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
    List<String> allMatchingTexts = ExtractMatchTextListHelper.extractAllMatchingTexts(
        messageStructure, messageStructure.getMessageData(),
        requestHitDto.getHit().getMatchingText());
    String fieldValue = FieldValueExtractor
        .extractFieldValues(messageStructure, messageStructure.getMessageData())
        .get(0)
        .stream()
        .findFirst()
        .orElse(null);
    List<String> allMatchingFieldValues = FieldValueExtractor
        .extractFieldValues(messageStructure, messageStructure.getMessageData())
        .stream()
        .findFirst()
        .orElse(Collections.emptyList());

    MessageFieldStructure messageFieldStructure = messageStructure.getMessageFieldStructure();

    Optional<String> accountNumberOrNormalizedName =
        new AccountNumberExtract(
            alertMessageDto.getApplicationCode(), requestHitDto.getHit().getTag(),
            messageStructure.getMessageData(), allMatchingFieldValues,
            messageFieldStructure).invoke();

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
        .build();
  }

  private String extractCountryCode(SourceSystem sourceSystem, AlertMessageDto alertMessageDto) {
    return countryCodeExtractor
        .invoke(new CountryCodeExtractRequest(
            sourceSystem,
            alertMessageDto.getUnit(),
            alertMessageDto.getIoIndicator(),
            alertMessageDto.getSenderCode(),
            alertMessageDto.getReceiverCode()
        ));
  }
}
