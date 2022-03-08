package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator;

import lombok.Builder;
import lombok.Getter;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.lexer.LexerEventListener;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtHit;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

class HitDetailsCollector implements LexerEventListener {

  private static final Pattern SUSPECT_DETECTED_PATTERN = Pattern.compile("Suspect detected #\\d+");
  private static final Pattern NEW_LINE_PATTERN = Pattern.compile("\\r?\\n");

  private final List<HitDetails> hitDetails = new ArrayList<>();

  private int lastSuspectLine = -1;
  private boolean waitingForOfacId = false;
  private String lastOfacId;

  @Override
  public void onStart() {
    // Do nothing.
  }

  @Override
  public void onEmptyLine() {
    // Do nothing.
  }

  @Override
  public void onFinish() {
    // Do nothing.
  }

  @Override
  public void onKey(int lineIndex, int positionIndex, String key) {
    if (key.equalsIgnoreCase("OFAC ID"))
      waitingForOfacId = true;
  }

  @Override
  public void onText(int lineIndex, int positionIndex, String text) {
    if (waitingForOfacId) {
      lastOfacId = text;
      waitingForOfacId = false;
    } else {
      Matcher matcher = SUSPECT_DETECTED_PATTERN.matcher(text);
      if (matcher.find())
        lastSuspectLine = lineIndex;
    }
  }

  @Override
  public void onSectionDivider(int lineIndex, int positionIndex) {
    if (lastSuspectLine >= 0 && lastOfacId != null) {
      int firstLine = Math.min(lastSuspectLine + 1, lineIndex);
      if (firstLine < lineIndex) {
        HitDetails hitDetailsEntry = HitDetails
            .builder()
            .ofacId(lastOfacId)
            .firstLine(firstLine)
            .lastLine(lineIndex)
            .build();

        hitDetails.add(hitDetailsEntry);
      }
    }

    lastSuspectLine = -1;
    lastOfacId = null;
  }

  @Override
  public void onListPrefix(int lineIndex, int positionIndex) {
    // Do nothing.
  }

  @Override
  public void onActiveListPrefix(int lineIndex, int positionIndex) {
    // Do nothing.
  }

  @Override
  public void onError(int lineIndex, int positionIndex, String text) {
    // Do nothing.
  }

  public List<GnsRtHit> asGnsRtHits(String hitsDetails) {
    List<String> lines = Arrays.asList(NEW_LINE_PATTERN.split(hitsDetails));

    return hitDetails
        .stream()
        .map(p -> p.makeGnsRtHit(lines))
        .collect(toList());
  }

  @Getter
  @Builder
  private static class HitDetails {

    private final String ofacId;
    private final int firstLine;
    private final int lastLine;

    @Nonnull
    GnsRtHit makeGnsRtHit(List<String> lines) {
      String encodedHitDetails = extractAndEncodeHitDetails(lines);

      GnsRtHit hit = new GnsRtHit();
      hit.setHitId(ofacId);
      hit.setHitDetails(encodedHitDetails);

      return hit;
    }

    @Nonnull
    private String extractAndEncodeHitDetails(List<String> lines) {
      String hitDetails = String.join("\n", lines.subList(firstLine, lastLine)).trim() + "\n";
      return Base64.getEncoder().encodeToString(hitDetails.getBytes(StandardCharsets.UTF_8));
    }
  }
}
