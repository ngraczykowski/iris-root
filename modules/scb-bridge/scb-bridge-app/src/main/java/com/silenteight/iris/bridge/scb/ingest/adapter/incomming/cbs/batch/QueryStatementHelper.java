/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class QueryStatementHelper {

  public static final String DB_RELATION_NAME_PARAM = ":dbRelationName";
  private static final Pattern WHITESPACE = Pattern.compile("[\\t\\n\\r]+");

  public static String prepareQuery(String query, String dbRelationName, Collection<String> ids) {
    if (log.isTraceEnabled()) {
      log.trace("Executing SQL: dbRelationName={}, query={}", dbRelationName,
          WHITESPACE.matcher(query).replaceAll(" "));
    }

    return prepareQuery(query.replace(DB_RELATION_NAME_PARAM, dbRelationName), ids);
  }

  static String prepareQuery(String query, Collection<String> ids) {
    return String.format(query, String.join(",", Collections.nCopies(ids.size(), "?")));
  }

  public static void setQueryParameters(PreparedStatement stat, Collection<String> ids) throws
      SQLException {
    int systemIdIdx = 1;
    for (String systemId : ids) {
      stat.setString(systemIdIdx, systemId);
      systemIdIdx++;
    }
  }
}
