package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonParser {

  private static final String HITS_PATH = "$.Hits";

  private final ParseContext parseContext;

  public List<JsonNode> getHits(String json) {
    TypeRef<List<JsonNode>> typeRef = new TypeRef<>() {};

    return parseContext.parse(json)
        .read(HITS_PATH, typeRef);
  }
}
