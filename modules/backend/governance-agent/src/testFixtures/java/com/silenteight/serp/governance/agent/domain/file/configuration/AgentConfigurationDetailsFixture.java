package com.silenteight.serp.governance.agent.domain.file.configuration;

public class AgentConfigurationDetailsFixture {

  public static final String AGENT_CONF_DATE_ENT_NORMAL = "date-entity-normal";
  public static final String AGENT_CONF_DATE_ENT_NORMAL_FILE =
      "/agents/configuration/date-entity-normal.yml";
  public static final String AGENT_CONF_DATE_ENT_STRICT = "date-entity-strict";
  public static final String AGENT_CONF_DATE_ENT_STRICT_FILE =
      "/agents/configuration/date-entity-strict.yml";
  public static final String AGENT_CONF_DATE_INDV_NORMAL = "date-individual-normal";
  public static final String AGENT_CONF_DATE_INDV_NORMAL_FILE =
      "/agents/configuration/date-indv-normal.yml";

  public static final AgentDetailsConfigurationDto AGENT_DETAILS_CONF_DATE_INDV_NORMAL_DTO
      = AgentDetailsConfigurationDto
      .builder()
      .name(AGENT_CONF_DATE_INDV_NORMAL)
      .configFile(AGENT_CONF_DATE_INDV_NORMAL_FILE)
      .build();

  public static final AgentDetailsConfigurationDto AGENT_DETAILS_CONF_DATE_NORMAL_DTO
      = AgentDetailsConfigurationDto
      .builder()
      .name(AGENT_CONF_DATE_ENT_NORMAL)
      .configFile(AGENT_CONF_DATE_ENT_NORMAL_FILE)
      .build();

  public static final AgentDetailsConfigurationDto AGENT_DETAILS_CONF_DATE_STRICT_DTO
      = AgentDetailsConfigurationDto
      .builder()
      .name(AGENT_CONF_DATE_ENT_STRICT)
      .configFile(AGENT_CONF_DATE_ENT_STRICT_FILE)
      .build();

  public static final String DATE_INDV_NORMAL_OUTPUT = ""
      + "{\"solvers.date\":"
      + "{\"generation\":"
      + "{\"min-year\":1900,"
      + "\"max-year\":2100}"
      + ",\"boundary\":{\"min-year\":1902,\"max-year\":2100},"
      + "\"delimiters\":[\" \",\",\"],"
      + "\"inconclusivePatterns\":[\" to \"],"
      + "\"result-definitions\":["
      + "{\"name\":\"EXACT\",\"dates\":[\"0-0-0\",\"0-0-X\",\"X-0-0\"]},"
      + "{\"name\":\"OUT_OF_RANGE\"}],"
      + "\"custom-formats\":[\"MMMM, yyyy\",\"MMMM,yyyy\",\"MMM, yyyy\"]}}";
}
