package com.silenteight.customerbridge.gnsrt.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;

@Validated
@Data
public class GnsRtResponseAlerts {

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

  /**
   * The action Silent Eight is recommend to take for this alert.
   */
  public enum RecommendationEnum {
    INVESTIGATE("INVESTIGATE"),

    FALSE_POSITIVE("FALSE_POSITIVE"),

    POTENTIAL_TRUE_POSITIVE("POTENTIAL_TRUE_POSITIVE");

    private String value;

    RecommendationEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static RecommendationEnum fromValue(String text) {
      for (RecommendationEnum b : RecommendationEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }
}
