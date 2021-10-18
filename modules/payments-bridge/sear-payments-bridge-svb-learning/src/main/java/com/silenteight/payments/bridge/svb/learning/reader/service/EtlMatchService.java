package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractAlertedPartyDataUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractFieldValueUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class EtlMatchService {

  private final ExtractAlertedPartyDataUseCase extractAlertedPartyDataUseCase;
  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;
  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;
  private final ExtractFieldValueUseCase extractFieldValueUseCase;

  LearningMatch fromLearningRows(List<LearningCsvRow> rows) {
    var alertedPartyData = createAlertedPartyData(rows);
    var matchingTexts = createMatchingTexts(rows);
    var messageStructure = createMessageStructure(rows, matchingTexts);

    return LearningMatch
        .builder()
        .matchId(rows.get(0).getFkcoVListFmmId())
        .alertedPartyData(alertedPartyData)
        .watchlistNames(createUniqueList(rows, LearningCsvRow::getFkcoVListName))
        .entityType(toEntityType(rows.get(0).getFkcoVListType()))
        .watchlistLocation(assertUnique(rows, row ->
            String.join(
                ", ",
                List.of(row.getFkcoVListCity() + row.getFkcoVListState()
                    + row.getFkcoVListCountry()))))
        .watchlistCountry(assertUnique(rows, LearningCsvRow::getFkcoVListCountry))
        .matchedFieldValue(rows.get(0).getFkcoVMatchedTagContent())
        .messageData(rows.get(0).getFkcoVContent())
        .messageStructure(messageStructure)
        .matchType(assertUnique(rows, LearningCsvRow::getFkcoVListType))
        .matchingTexts(matchingTexts)
        .applicationCode(rows.get(0).getFkcoVApplication())
        .hitTag(rows.get(0).getFkcoVMatchedTag())
        .allMatchFieldsValue(createAllMatchFieldsValue(messageStructure))
        .matchedNames(createUniqueList(rows, LearningCsvRow::getFkcoVListMatchedName))
        .matchedCountries(createUniqueList(rows, LearningCsvRow::getFkcoVListCountry))
        .matchedCountries(createUniqueList(rows, LearningCsvRow::getFkcoVHitType))
        .alertedPartyEntity(createAlertedPartyEntities(alertedPartyData, matchingTexts))
        .watchlistType(WatchlistType.ofCode(rows.get(0).getFkcoVListType()))
        .build();
  }

  private List<String> createAllMatchFieldsValue(AbstractMessageStructure messageStructure) {
    return extractFieldValueUseCase
        .extractFieldValues(messageStructure)
        .stream()
        .flatMap(List::stream)
        .collect(toList());
  }

  private AlertedPartyData createAlertedPartyData(List<LearningCsvRow> rows) {
    var row = rows.get(0);
    return extractAlertedPartyDataUseCase.extractAlertedPartyData(
        ExtractAlertedPartyDataRequest
            .builder()
            .messageData(row.getFkcoVContent())
            .messageType(row.getFkcoVType())
            .matchingText(String.join(",", createMatchingTexts(rows)))
            .tag(row.getFkcoVMatchedTag())
            .applicationCode(row.getFkcoVApplication())
            .build());
  }

  private AbstractMessageStructure createMessageStructure(
      List<LearningCsvRow> rows, List<String> matchingTexts) {
    var row = rows.get(0);
    return extractMessageStructureUseCase.extractMessageStructure(
        ExtractAlertedPartyDataRequest
            .builder()
            .applicationCode(row.getFkcoVApplication())
            .messageData(row.getFkcoVContent())
            .messageType(row.getFkcoVType())
            .tag(row.getFkcoVMatchedTag())
            .matchingText(StringUtils.join(matchingTexts, ","))
            .build());
  }

  private static List<String> createMatchingTexts(List<LearningCsvRow> rows) {
    List<String> matchingText = SetUniqueList.setUniqueList(new ArrayList<>());

    rows.forEach(row -> {
      matchingText.add(row.getFkcoVCityMatchedText());
      matchingText.add(row.getFkcoVAddressMatchedText());
      matchingText.add(row.getFkcoVCityMatchedText());
      matchingText.add(row.getFkcoVStateMatchedText());
      matchingText.add(row.getFkcoVCountryMatchedText());
    });

    return matchingText
        .stream()
        .filter(s -> !s.equalsIgnoreCase("N/A"))
        .collect(toList());
  }


  private static String assertUnique(
      List<LearningCsvRow> rows, Function<LearningCsvRow, String> rowFunc) {
    var first = rowFunc.apply(rows.get(0));
    for (var row : rows) {
      var rowVal = rowFunc.apply(row);
      if (!rowVal.equals(first)) {
        throw new NotUniqueValueException();
      }
    }
    return first;
  }

  private static List<String> createUniqueList(
      List<LearningCsvRow> rows, Function<LearningCsvRow, String> rowFunc) {
    List<String> uniqueList = SetUniqueList.setUniqueList(new ArrayList<>());
    rows.forEach(row -> uniqueList.add(rowFunc.apply(row)));
    return uniqueList;
  }


  private static EntityType toEntityType(String type) {
    HashMap<String, EntityType> types = new HashMap<>();
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

  private static class NotUniqueValueException extends RuntimeException {

    private static final long serialVersionUID = 2491372116858557898L;

    NotUniqueValueException() {
      super("Not unique value for match");
    }
  }
}
