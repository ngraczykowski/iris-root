package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage;
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit;
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.proto.fab.api.v1.AlertMessageDetails;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.silenteight.fab.dataprep.infrastructure.FeatureAndCategoryConfiguration.LIST_OF_STRINGS;
import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@Service
@RequiredArgsConstructor
public class AlertParser {

  private static final String HITS_PATH = "$.Message.Hits.*.Hit";

  private static final String MESSAGE_DATA_PATH = "$.Message.MessageData";

  private static final String SYSTEM_ID_PATH = "$.Message.SystemID";

  private static final String OFAC_ID_PATH = "$.HittedEntity.ID";

  private final ParseContext parseContext;

  private final MessageDataTokenizer messageDataTokenizer;

  public ParsedAlertMessage parse(AlertMessageStored message, AlertMessageDetails alertDetails) {
    DocumentContext documentContext = parseContext.parse(alertDetails.getPayload());

    return ParsedAlertMessage.builder()
        .batchName(message.getBatchName())
        .messageName(message.getMessageName())
        .parsedMessageData(parseMessageData(documentContext))
        .systemId(getSystemId(documentContext))
        .hits(getMatches(documentContext))
        .build();
  }

  private ParsedMessageData parseMessageData(DocumentContext documentContext) {
    List<String> nodes = documentContext.read(MESSAGE_DATA_PATH, LIST_OF_STRINGS);
    return messageDataTokenizer.convert(nodes.stream().findFirst().orElse(EMPTY));
  }

  private Map<String, Hit> getMatches(DocumentContext documentContext) {
    List<JsonNode> hits = getHits(documentContext);
    Collection<List<JsonNode>> mergedHits = mergeHits(hits);
    return mergedHits
        .stream()
        .map(AlertParser::convert)
        .collect(toMap(Hit::getHitName, identity()));
  }

  private static Hit convert(List<JsonNode> jsonNodes) {
    return Hit.builder().hitName(UUID.randomUUID().toString()).payloads(jsonNodes).build();
  }

  private static List<JsonNode> getHits(DocumentContext documentContext) {
    TypeRef<List<JsonNode>> typeRef = new TypeRef<>() {};
    return documentContext.read(HITS_PATH, typeRef);
  }

  private static String getSystemId(DocumentContext documentContext) {
    return documentContext.read(SYSTEM_ID_PATH, LIST_OF_STRINGS).stream()
        .findFirst()
        .orElse(null);
  }

  Collection<List<JsonNode>> mergeHits(List<JsonNode> hits) {
    return hits.stream()
        .collect(groupingBy(this::getOfacId, toList()))
        .values();
  }

  private String getOfacId(JsonNode hit) {
    return parseContext.parse(hit).read(OFAC_ID_PATH, LIST_OF_STRINGS)
        .stream()
        .findFirst()
        .orElseGet(() -> UUID.randomUUID().toString());
  }
}
