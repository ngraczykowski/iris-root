package com.silenteight.serp.governance.migrate;

import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;

@Getter
public class MigrationMetadata {

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "UTC")
  private Instant exportedAt;

  public static MigrationMetadata build(Instant instant) {
    MigrationMetadata metadata = new MigrationMetadata();
    metadata.exportedAt = instant;
    return metadata;
  }

  private MigrationMetadata() {
  }
}
