package com.silenteight.serp.governance.agent.list;

import com.silenteight.serp.governance.agent.list.dto.ListAgentDto;

import java.util.List;

public interface ListAgentQuery {

  List<ListAgentDto> list();
}
