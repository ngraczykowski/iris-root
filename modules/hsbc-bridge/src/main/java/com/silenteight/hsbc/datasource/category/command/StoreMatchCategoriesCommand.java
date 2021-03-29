package com.silenteight.hsbc.datasource.category.command;

import lombok.Builder;
import lombok.Value;

import com.silenteight.hsbc.bridge.match.MatchComposite;

import java.util.List;

@Builder
@Value
public class StoreMatchCategoriesCommand {

  List<MatchComposite> matchComposites;
}
