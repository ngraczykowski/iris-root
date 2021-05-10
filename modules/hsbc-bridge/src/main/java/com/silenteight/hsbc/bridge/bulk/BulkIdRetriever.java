package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.Optional;

import static com.fasterxml.jackson.core.JsonToken.END_OBJECT;
import static com.fasterxml.jackson.core.JsonToken.VALUE_STRING;
import static java.util.Objects.nonNull;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
class BulkIdRetriever {

  private static final String BULK_ID_KEY = "bulkId";

  private final JsonFactory jsonFactory = new JsonFactory();

  Optional<String> retrieve(@NonNull String json) {
    try (var parser = jsonFactory.createParser(json)) {
      parser.nextToken();
      while (isNotLastToken(parser.nextToken())) {
        if (BULK_ID_KEY.equals(parser.getCurrentName())) {
          if (parser.nextToken() == VALUE_STRING) {
            return of(parser.getValueAsString());
          }
        } else {
          parser.skipChildren();
        }
      }
    } catch (IOException exception) {
      log.error("Cannot locate 'bulkId' key", exception);
    }

    return empty();
  }

  private boolean isNotLastToken(JsonToken token) {
    return nonNull(token) && token != END_OBJECT;
  }
}
