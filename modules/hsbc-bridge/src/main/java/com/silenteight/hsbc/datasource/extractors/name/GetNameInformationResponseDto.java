package com.silenteight.hsbc.datasource.extractors.name;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Builder
@Value
public class GetNameInformationResponseDto {

  String firstName;
  String lastName;
  List<String> aliases;
  List<ForeignAliasDto> foreignAliases;
}
