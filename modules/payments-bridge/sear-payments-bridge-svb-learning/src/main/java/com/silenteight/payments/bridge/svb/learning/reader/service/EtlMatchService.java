package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.etl.parser.domain.MessageFormat;
import com.silenteight.payments.bridge.etl.parser.port.MessageParserUseCase;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;

import org.apache.commons.collections4.list.SetUniqueList;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class EtlMatchService {

  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;
  private final MessageParserUseCase messageParserUseCase;

  LearningMatch fromLearningRows(LearningCsvRow row) {
    var matchingTexts = createMatchingTexts(row);
    var alertedPartyData =
        createAlertedPartyData(row);
    return LearningMatch
        .builder()
        .matchId(row.getMatchId())
        .alertedPartyData(alertedPartyData)
        .watchlistNames(List.of(row.getFkcoVListName().split(",")))
        .entityType(toEntityType(row.getFkcoVListType()))
        .watchlistLocation(
            String.join(
                ", ",
                List.of(
                    row.getFkcoVListCountry(), row.getFkcoVListState(), row.getFkcoVListCity())))
        .watchlistCountry(row.getFkcoVListCountry())
        .matchedFieldValue(row.getFkcoVMatchedTagContent())
        .messageData(row.getFkcoVContent())
        .matchType(row.getFkcoVListType())
        .ofacId(row.getFkcoVListFmmId())
        .matchingTexts(matchingTexts)
        .applicationCode(row.getFkcoVApplication())
        .hitTag(row.getFkcoVMatchedTag())
        .allMatchFieldsValue(createAllMatchFieldsValue(row))
        .matchedNames(new ArrayList<>(Arrays.asList(row.getFkcoVListMatchedName().split(","))))
        .matchedCountries(List.of(row.getFkcoVListCountry().split(",")))
        .hitType(row.getFkcoVHitType())
        .alertedPartyEntity(createAlertedPartyEntities(alertedPartyData, matchingTexts))
        .nameMatchedTexts(List.of(row.getFkcoVNameMatchedText()))
        .watchlistType(WatchlistType.ofCode(row.getFkcoVListType()))
        .build();
  }

  private List<String> createAllMatchFieldsValue(LearningCsvRow row) {
    var messageData = messageParserUseCase.parse(
        row.getFkcoVContent().startsWith("{") ? MessageFormat.SWIFT : MessageFormat.ALL,
        row.getFkcoVContent());

    return messageData.findAllValues(row.getFkcoVMatchedTag()).collect(toList());
  }

  private AlertedPartyData createAlertedPartyData(
      LearningCsvRow row) {
    var messageData = messageParserUseCase.parse(
        row.getFkcoVContent().startsWith("{") ? MessageFormat.SWIFT : MessageFormat.ALL,
        row.getFkcoVContent());
    return AlertParserService.extractAlertedPartyData(
        messageData, row.getFkcoVMatchedTag(),
        row.getFkcoVFormat(), row.getFkcoVApplication());
  }

  private static List<String> createMatchingTexts(LearningCsvRow row) {
    var matchingText = SetUniqueList.setUniqueList(new ArrayList<String>());

    matchingText.add(row.getFkcoVCityMatchedText());
    matchingText.add(row.getFkcoVAddressMatchedText());
    matchingText.add(row.getFkcoVCityMatchedText());
    matchingText.add(row.getFkcoVStateMatchedText());
    matchingText.add(row.getFkcoVCountryMatchedText());

    return matchingText
        .stream()
        .filter(s -> !s.equalsIgnoreCase("N/A"))
        .collect(toList());
  }


  private static EntityType toEntityType(String type) {
    var types = new HashMap<String, EntityType>();
    types.put("INDIVIDUAL", EntityType.INDIVIDUAL);
    types.put("COMPANY", EntityType.ORGANIZATION);

    if (types.containsKey(type))
      return types.get(type);

    return EntityType.ENTITY_TYPE_UNSPECIFIED;
  }

  private Map<AlertedPartyKey, String> createAlertedPartyEntities(
      AlertedPartyData alertedPartyData, List<String> matchingTexts) {

    return createAlertedPartyEntitiesUseCase.create(CreateAlertedPartyEntitiesRequest
        .builder()
        .alertedPartyData(alertedPartyData)
        .allMatchingText(matchingTexts)
        .build());
  }

}
