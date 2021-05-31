package com.silenteight.adjudication.engine.analysis.commentinput.jdbc;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@ConfigurationProperties(prefix = "ae.comment-input")
@Data
@Validated
public class CommentInputJdbcProperties {

  @Min(1)
  @Max(16_384)
  private int missingBatchSizeSelect;
}
