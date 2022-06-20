/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.decisiongroups;

import lombok.Data;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.validation.OracleRelationName;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "serp.scb.bridge.groups")
@Data
class JdbcDecisionGroupsReaderProperties {

  /**
   * Name of the database relation that will be queried for distinct units.
   */
  @OracleRelationName
  String relationName = JdbcDecisionGroupsReader.DEFAULT_RELATION_NAME;

  /**
   * Overrides default query.
   * <p/>
   * First column of the result must contain name of decision group.
   */
  String query;

  JdbcDecisionGroupsReader setupReader(JdbcDecisionGroupsReader reader) {
    reader.setRelationName(StringUtils.trim(relationName));

    if (StringUtils.isNotBlank(query))
      reader.setQuery(query.trim());

    return reader;
  }
}
