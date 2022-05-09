package com.silenteight.sep.usermanagement.api.event;

import com.silenteight.sep.usermanagement.api.event.dto.EventDto;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface EventQuery {

  List<EventDto> list(OffsetDateTime from, Collection<EventType> types);
}
