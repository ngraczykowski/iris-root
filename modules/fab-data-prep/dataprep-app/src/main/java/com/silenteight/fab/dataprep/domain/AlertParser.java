package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.fab.dataprep.domain.model.ExtractedAlert;
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match;
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.proto.fab.api.v1.AlertMessageDetails;
import com.silenteight.proto.fab.api.v1.AlertMessageStored;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.TypeRef;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class AlertParser {

  private static final String HITS_PATH = "$.Hits.*.Hit";

  private static final String MESSAGE_DATA_PATH = "$.MessageData";

  private final ParseContext parseContext;

  private final MessageDataTokenizer messageDataTokenizer;

  public ExtractedAlert parse(AlertMessageStored message, AlertMessageDetails alertDetails) {
    DocumentContext documentContext = parseContext.parse(alertDetails.getPayload());

    return ExtractedAlert.builder()
        .batchId(message.getBatchName())
        .alertId(message.getMessageName())
        .parsedMessageData(parseMessageData(documentContext))
        .matches(getMatches(documentContext))
        .build();
    //TODO set fields: alertName, status, errorDescription, matches
  }

  private ParsedMessageData parseMessageData(DocumentContext documentContext) {
    return messageDataTokenizer.convert(documentContext.read(MESSAGE_DATA_PATH, String.class));
  }

  private static Map<String, Match> getMatches(DocumentContext documentContext) {
    return getHits(documentContext)
        .stream()
        .map(AlertParser::convert)
        .collect(toMap(Match::getMatchId, identity()));
  }

  private static Match convert(JsonNode jsonNode) {
    return Match.builder().matchId(UUID.randomUUID().toString()).payload(jsonNode).build();
  }

  private static List<JsonNode> getHits(DocumentContext documentContext) {
    TypeRef<List<JsonNode>> typeRef = new TypeRef<>() {};
    return documentContext.read(HITS_PATH, typeRef);
  }
}
