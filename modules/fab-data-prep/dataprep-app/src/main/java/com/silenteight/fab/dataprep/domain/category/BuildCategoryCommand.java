package com.silenteight.fab.dataprep.domain.category;

import lombok.Builder;
import lombok.Value;

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData;

@Value
@Builder
public class BuildCategoryCommand {

  String matchName;
  String systemId;
  ParsedMessageData parsedMessageData;
}
