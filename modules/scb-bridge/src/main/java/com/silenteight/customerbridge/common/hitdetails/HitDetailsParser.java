package com.silenteight.customerbridge.common.hitdetails;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.hitdetails.builder.HitDetailsBuilder;
import com.silenteight.customerbridge.common.hitdetails.builder.HitDetailsBuilderConfig;
import com.silenteight.customerbridge.common.hitdetails.element.ElementCollector;
import com.silenteight.customerbridge.common.hitdetails.lexer.Lexer;
import com.silenteight.customerbridge.common.hitdetails.model.HitDetails;
import com.silenteight.customerbridge.gnsrt.model.request.GnsRtHit;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor
@Slf4j
public class HitDetailsParser {

  private final Decoder base64Decoder = Base64.getDecoder();
  private final HitDetailsBuilderConfig config = new HitDetailsBuilderConfig();

  public HitDetails parseHitDetailsFromGnsRt(@NonNull List<GnsRtHit> gnsRtHits) {
    var decodedHitDetails = gnsRtHits.stream()
        .map(GnsRtHit::getHitDetails)
        .map(String::trim)
        .map(s -> new String(base64Decoder.decode(s), StandardCharsets.UTF_8))
        .collect(Collectors.toList());

    StringBuilder hitDetailsTextBuilder = new StringBuilder();
    for (int index = 0; index < decodedHitDetails.size(); index++) {
      hitDetailsTextBuilder.append("=============================\n");
      hitDetailsTextBuilder.append("Suspect detected #").append(index + 1).append("\n\n");
      hitDetailsTextBuilder.append(decodedHitDetails.get(index));
      hitDetailsTextBuilder.append("\n=============================");
    }

    return parse(hitDetailsTextBuilder.toString());
  }

  public HitDetails parse(@Nullable String text) {
    requireNonNull(text, "Hit details text is null.");
    try {
      HitDetailsBuilder hitDetailsBuilder = new HitDetailsBuilder(config);
      ElementCollector elementCollector = new ElementCollector(hitDetailsBuilder);
      Lexer lexer = new Lexer(elementCollector);
      lexer.lex(text);
      return hitDetailsBuilder.build();
    } catch (Exception e) {
      throw new ParserException(e);
    }
  }

  public static class ParserException extends RuntimeException {

    private static final long serialVersionUID = 3833193138616747277L;

    ParserException(Throwable throwable) {
      super(throwable);
    }
  }
}
