package com.silenteight.fab.dataprep.domain.category;

import lombok.Builder;
import lombok.Value;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert;

@Value
@Builder
public class BuildCategoryCommand {

  String systemId;
  ParsedMessageData parsedMessageData;
  RegisteredAlert.Match match;
}
