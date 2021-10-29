package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.name.v1.NameFeatureInput.EntityType;
import com.silenteight.payments.bridge.agents.model.AlertedPartyKey;
import com.silenteight.payments.bridge.common.dto.common.WatchlistType;
import com.silenteight.payments.bridge.etl.firco.parser.MessageFormat;
import com.silenteight.payments.bridge.etl.firco.parser.MessageParserFacade;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;
import com.silenteight.payments.bridge.svb.oldetl.model.AbstractMessageStructure;
import com.silenteight.payments.bridge.svb.oldetl.model.CreateAlertedPartyEntitiesRequest;
import com.silenteight.payments.bridge.svb.oldetl.model.ExtractAlertedPartyDataRequest;
import com.silenteight.payments.bridge.svb.oldetl.port.CreateAlertedPartyEntitiesUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractFieldValueUseCase;
import com.silenteight.payments.bridge.svb.oldetl.port.ExtractMessageStructureUseCase;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;
import com.silenteight.payments.bridge.svb.oldetl.service.AlertParserService;

import org.apache.commons.collections4.list.SetUniqueList;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
class EtlMatchService {

  private final CreateAlertedPartyEntitiesUseCase createAlertedPartyEntitiesUseCase;
  private final ExtractMessageStructureUseCase extractMessageStructureUseCase;
  private final ExtractFieldValueUseCase extractFieldValueUseCase;
  private final MessageParserFacade messageParserFacade;

  LearningMatch fromLearningRows(LearningCsvRow row) {
    var matchingTexts = createMatchingTexts(row);
    var messageStructure = createMessageStructure(row, matchingTexts);
    var alertedPartyData =
        createAlertedPartyData(row, messageStructure.getMessageFieldStructure());

    var matchId =
        row.getFkcoVListFmmId() + "(" + row.getFkcoVMatchedTag() + ", #" + row.getFkcoISequence()
            + ")";

    return LearningMatch
        .builder()
        .matchId(matchId)
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
        .messageStructure(messageStructure)
        .matchType(row.getFkcoVListType())
        .ofacId(row.getFkcoVListFmmId())
        .matchingTexts(matchingTexts)
        .applicationCode(row.getFkcoVApplication())
        .hitTag(row.getFkcoVMatchedTag())
        .allMatchFieldsValue(createAllMatchFieldsValue(messageStructure))
        .matchedNames(new ArrayList<>(Arrays.asList(row.getFkcoVListMatchedName().split(","))))
        .matchedCountries(List.of(row.getFkcoVListCountry().split(",")))
        .hitType(row.getFkcoVHitType())
        .alertedPartyEntity(createAlertedPartyEntities(alertedPartyData, matchingTexts))
        .nameMatchedTexts(List.of(row.getFkcoVNameMatchedText()))
        .watchlistType(WatchlistType.ofCode(row.getFkcoVListType()))
        .build();
  }

  private List<String> createAllMatchFieldsValue(AbstractMessageStructure messageStructure) {
    return extractFieldValueUseCase
        .extractFieldValues(messageStructure)
        .stream()
        .flatMap(List::stream)
        .collect(toList());
  }

  private AlertedPartyData createAlertedPartyData(
      LearningCsvRow row, MessageFieldStructure messageFieldStructure) {
    var messageData = messageParserFacade.parse(
        row.getFkcoVContent().startsWith("{") ? MessageFormat.SWIFT : MessageFormat.ALL,
        row.getFkcoVContent());
    return AlertParserService.extractAlertedPartyData(
        messageData, row.getFkcoVMatchedTag(), messageFieldStructure,
        row.getFkcoVFormat());
  }

  private AbstractMessageStructure createMessageStructure(
      LearningCsvRow row, List<String> matchingTexts) {
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

    var uniqueList = SetUniqueList.setUniqueList(new ArrayList<String>());
    rows.forEach(row -> uniqueList.add(rowFunc.apply(row)));
    return uniqueList;
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

  private static class NotUniqueValueException extends RuntimeException {

    private static final long serialVersionUID = 2491372116858557898L;

    NotUniqueValueException() {
      super("Not unique value for match");
    }
  }
}
