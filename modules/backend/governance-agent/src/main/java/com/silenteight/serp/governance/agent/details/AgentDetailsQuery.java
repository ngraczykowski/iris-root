package com.silenteight.serp.governance.agent.details;

import lombok.NonNull;

import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;

public interface AgentDetailsQuery {

  AgentDetailsDto details(@NonNull String id);
}
