package com.silenteight.customerbridge.common.decisiongroups;

import lombok.Data;

import com.silenteight.customerbridge.common.validation.OracleRelationName;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.silenteight.customerbridge.common.decisiongroups.JdbcDecisionGroupsReader.DEFAULT_RELATION_NAME;

@ConfigurationProperties(prefix = "serp.scb.bridge.groups")
@Data
class JdbcDecisionGroupsReaderProperties {

  /**
   * Name of the database relation that will be queried for distinct units.
   */
  @OracleRelationName
  String relationName = DEFAULT_RELATION_NAME;

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
