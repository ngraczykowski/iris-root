package com.silenteight.searpayments.scb.etl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.dto.input.RequestHitDto;
import com.silenteight.payments.bridge.metrics.LogMethodTime;
import com.silenteight.payments.bridge.model.SolutionType;
import com.silenteight.payments.bridge.model.WatchlistType;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractRequest;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractor;
import com.silenteight.searpayments.scb.etl.response.*;
import com.silenteight.searpayments.scb.etl.utils.*;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AlertParser {

  private final CountryCodeExtractor countryCodeExtractor;
  private final AlertMessageDto alertMessageDto;
  private AbstractMessageStructure.SourceSystem sourceSystem;

  /*
     Alert parsing stages:

     1st -> validation of the input message (sourceSystem, messageType, apTag) to check if the Hit
            is useful for "structured" agents (i.e. NameAgent, GeoAgent, CrossMatchAgent, ...)

     2nd -> recognition of the messageFieldStructure (thee format of the field with matched data)
            -> NAMEADDRESS_FORMAT_F
            -> NAMEADDRESS_FORMAT_UNSTRUCTURED
            -> UNSTRUCTURED

     3rd -> data extraction and creation of the result object - the list of HitData objects, which
          consist of pairs:
            - alertedPartyData
            - hitAndWatchlistPartyData
  */

  @LogMethodTime
  public AlertEtlResponse invoke() {
    log.debug("invoke");
    List<HitData> hits = new ArrayList<>();

    for (RequestHitDto requestHitDto : alertMessageDto.getHits()) {
      hits.add(createHitData(requestHitDto));
    }

    return AlertEtlResponse.builder()
        .systemId(alertMessageDto.getSystemId())
        .messageId(alertMessageDto.getMessageId())
        .messageType(alertMessageDto.getMessageType())
        .applicationCode(alertMessageDto.getApplicationCode())
        .messageFormat(alertMessageDto.getMessageFormat())
        .messageData(alertMessageDto.getMessageData())
        .businessUnit(alertMessageDto.getBusinessUnit())
        .unit(alertMessageDto.getUnit())
        .senderReference(alertMessageDto.getSenderReference())
        .ioIndicator(alertMessageDto.getIoIndicator())
        .currentStatus(AlertStatusExtractor.retrieveAlertStatus(alertMessageDto.getCurrentStatus()))
        .countryCode(extractCountryCode())
        .hits(hits)
        .build();
  }

  private HitData createHitData(RequestHitDto requestHitDto) {
    AbstractMessageStructure
        messageStructure = extractMessageStructure(requestHitDto);
    AlertedPartyData alertedPartyData =
        extractAlertedPartyData(messageStructure);
    HitAndWatchlistPartyData hitAndWatchlistPartyData =
        extractHitAndWatchlistPartyData(requestHitDto, messageStructure);
    return new HitData(alertedPartyData, hitAndWatchlistPartyData);
  }

  private String extractCountryCode() {
    return countryCodeExtractor
        .invoke(new CountryCodeExtractRequest(
            sourceSystem,
            alertMessageDto.getUnit(),
            alertMessageDto.getIoIndicator(),
            alertMessageDto.getSenderCode(),
            alertMessageDto.getReceiverCode()
        ));
  }

  @LogMethodTime
  private AbstractMessageStructure extractMessageStructure(
      RequestHitDto requestHitDto) {

    String messageType = alertMessageDto.getMessageType();
    String tag = requestHitDto.getHit().getTag();
    String messageData = alertMessageDto.getMessageData();
    sourceSystem =
        AbstractMessageStructure.extractSourceSystem(alertMessageDto.getApplicationCode());
    log.debug("sourceSystem: {}, messageType: {}, tag: {}",
        sourceSystem, messageType, tag);

    switch (sourceSystem) {
      case SCSTAR:
        return messageStructureForScstar(messageType, tag, messageData);
      case MTS:
        return messageStructureForMts(messageType, tag, messageData, sourceSystem);
      case NBP:
        return messageStructureForNbp(messageType, tag, messageData);
      case STS:
        return messageStructureForSts(messageType, tag, messageData);
      case DTP:
        return messageStructureForDtp(requestHitDto, messageType, tag, messageData);
      default:
        return defaultMessageStructure(messageType, tag, messageData);
    }
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureDefault defaultMessageStructure(
      String messageType, String tag, String messageData) {
    return new AbstractMessageStructure.MessageStructureDefault(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureDtp messageStructureForDtp(
      RequestHitDto requestHitDto, String messageType, String tag, String messageData) {
    String matchText = null;
    List<String> mainTagFieldValues = null;
    List<String> nextTagFieldValues = null;
    if (AbstractMessageStructure.MessageStructureDtp.DTP_PAIRS_SCOPE.contains(tag)) {
      matchText = requestHitDto.getHit().getMatchingText();
      List<List<String>> components =
          ComponentsExtractor.getComponents(
              alertMessageDto.getMessageData(), tag, matchText);
      mainTagFieldValues = ComponentExtractorHelper.getElementsFromComponents(components, 0);
      nextTagFieldValues = ComponentExtractorHelper.getElementsFromComponents(components, 2);
    }
    return new AbstractMessageStructure.MessageStructureDtp(messageType, tag, messageData,
        matchText, mainTagFieldValues,
        nextTagFieldValues);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureSts messageStructureForSts(
      String messageType, String tag, String messageData) {
    return new AbstractMessageStructure.MessageStructureSts(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureNbp messageStructureForNbp(
      String messageType, String tag, String messageData) {
    return new AbstractMessageStructure.MessageStructureNbp(messageType, tag, messageData);
  }

  @NotNull
  private AbstractMessageStructure.MessageStructureMts messageStructureForMts(
      String messageType, String tag, String messageData,
      AbstractMessageStructure.SourceSystem sourceSystem) {
    return new AbstractMessageStructure.MessageStructureMts(
        messageType, tag, messageData, FieldValueExtractor.extractFieldValue(
        sourceSystem.name(), tag, alertMessageDto.getMessageData()));
  }

  private AbstractMessageStructure.MessageStructureScstar messageStructureForScstar(
      String messageType, String tag, String messageData) {
    return new AbstractMessageStructure.MessageStructureScstar(messageType, tag, messageData);
  }

  private AlertedPartyData extractAlertedPartyData(AbstractMessageStructure messageStructure) {

    List<List<String>> fieldValues =
        FieldValueExtractor.extractFieldValues(
            alertMessageDto.getApplicationCode(),
            messageStructure,
            alertMessageDto.getMessageData());

    MessageFieldStructure messageFieldStructure = messageStructure.getMessageFieldStructure();
    MessageFieldExtractor.MessageNameAddressResult messageNameAddressResult =
        MessageFieldExtractor.extractNameAddress(
            fieldValues,
            messageFieldStructure,
            messageStructure.checkMessageWithoutAccountNum());

    return AlertedPartyData.builder()
        .names(messageNameAddressResult.getNames())
        .addresses(messageNameAddressResult.getAddresses())
        .ctryTowns(messageNameAddressResult.getCtryTowns())
        .nameAddresses(messageNameAddressResult.getNameAddresses())
        .noAcctNumFlag(messageNameAddressResult.isNoAcctNumFlag())
        .numOfLines(messageNameAddressResult.getNumOfLines())
        .messageLength(messageNameAddressResult.getLength())
        .messageFieldStructure(messageFieldStructure)
        .build();
  }

  private HitAndWatchlistPartyData extractHitAndWatchlistPartyData(
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
}
