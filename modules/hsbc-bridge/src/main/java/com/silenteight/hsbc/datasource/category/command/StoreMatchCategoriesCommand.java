package com.silenteight.hsbc.datasource.category.command;

import lombok.Value;

import com.silenteight.hsbc.bridge.match.MatchComposite;

import java.util.List;

@Value
public class StoreMatchCategoriesCommand {

  List<MatchComposite> matchComposites;
}
