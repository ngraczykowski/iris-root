package com.silenteight.serp.governance.changerequest.list;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import java.util.Collection;
import java.util.Set;

public interface ListChangeRequestsQuery {

  Collection<ChangeRequestDto> list(Set<ChangeRequestState> states);
}
