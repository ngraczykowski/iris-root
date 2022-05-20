package com.silenteight.payments.bridge.agents.service;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "pb.agents.specific-terms")
class SpecificTermsProperties {

  private static final String DEFAULT_REGULAR_TERMS =
      "([12]/|\\n|\\\\s)S(/|\\\\s)O([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)P.?O.?([12]/|\\n|\\\\s)?BOX"
          + "([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s|,)P.?([12]/|\\n|\\\\s)?B.([12]/|\\n|\\\\s)?[0-9]"
          + "{3,9}([12]/|\\n|\\\\s|,),,([12]/|\\n|\\\\s|,)PO([12]/|\\n|\\\\s)?[0-9]{3,9}([12]/|\\"
          + "n|\\\\s|,),,([12]/|\\n|\\\\s)D(/|\\\\s)O([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)C(/|\\\\"
          + "s)O([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)FORMALLY([12]/|\\n|\\\\s)KNOWN([12]/|\\n|\\\\s"
          + ")AS([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)ON([12]/|\\n|\\\\s)BEHALF([12]/|\\n|\\\\s)OF("
          + "[12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)FOR([12]/|\\n|\\\\s)FURTHER([12]/|\\n|\\\\s)CREDI"
          + "T([12]/|\\n|\\\\s),,([12]/|\\n|\\\\s)ATT(ENTIO)?N([12]/|\\n|\\\\s)?([12]/|\\n|\\\\"
          + "s|:),,([12]/|\\n|\\\\s)O/B([12]/|\\n|\\\\s),,OWNED([12]/|\\n|\\\\s)BY([12]/|\\n|\\\\"
          + "s),,OFFICE([12]/|\\n|\\\\s)FROM([12]/|\\n|\\\\s),,MEMBER([12]/|\\n|\\\\s)OF([12]/|\\n"
          + "|\\\\s),,([12]/|\\n|\\\\s)T/A([12]/|\\n|\\\\s)";

  private String regularTerms = DEFAULT_REGULAR_TERMS;

}
