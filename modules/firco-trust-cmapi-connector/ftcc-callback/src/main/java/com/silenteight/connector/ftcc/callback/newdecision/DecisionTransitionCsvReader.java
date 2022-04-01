package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class DecisionTransitionCsvReader {

  static List<DecisionTransition> decision(Reader reader) {
    var builder =
        new CsvToBeanBuilder<DecisionTransition>(reader)
            .withType(DecisionTransition.class)
            .build();

    return builder.parse();
  }
}
