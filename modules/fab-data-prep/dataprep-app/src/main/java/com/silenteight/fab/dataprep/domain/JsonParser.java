package com.silenteight.fab.dataprep.domain;

import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonParser {
  private final ParseContext parseContext;

  public List<JsonNode> getHits(String json) {
    ReadContext documentContext = parseContext.parse(json);
    TypeRef<List<JsonNode>> typeRef = new TypeRef<List<JsonNode>>() {};

    return documentContext.read("$.Hits", typeRef);
  }
}
