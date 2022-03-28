package com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotNull;

@Validated
@Data
@Builder
public class GnsRtResponseAlert {

  @JsonProperty("alertId")
  @NotNull
  private String alertId;

  @JsonProperty("comments")
  private String comments;

  @JsonProperty("recommendation")
  @NotNull
  private RecommendationEnum recommendation;

  @JsonProperty("recommendationTimestamp")
  @NotNull
  private LocalDateTime recommendationTimestamp;

  @JsonProperty("watchlistType")
  @NotNull
  private String watchlistType;

  @JsonProperty("policyId")
  private String policyId;

  @JsonProperty("matches")
  private List<GnsRtResponseMatch> matches;

  /**
   * The action Silent Eight is recommend to take for this alert.
   */
  @AllArgsConstructor
  public enum RecommendationEnum {
    INVESTIGATE("INVESTIGATE"),

    FALSE_POSITIVE("FALSE_POSITIVE"),

    POTENTIAL_TRUE_POSITIVE("POTENTIAL_TRUE_POSITIVE");

    private final String value;

    @JsonCreator
    public static RecommendationEnum fromValue(String text) {
      for (RecommendationEnum b : RecommendationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalStateException(
          "Can't find matching RecommendationEnum value for action: " + text);
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }
}
