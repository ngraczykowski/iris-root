package com.silenteight.hsbc.datasource.dto.name;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GetNameInformationResponseDto {

  List<ForeignAliasDto> foreignAliases;
}
