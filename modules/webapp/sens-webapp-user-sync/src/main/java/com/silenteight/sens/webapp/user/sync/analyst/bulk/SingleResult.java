package com.silenteight.sens.webapp.user.sync.analyst.bulk;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SingleResult {

  @NonNull
  private final Result result;
  @Nullable
  private final String message;

  public static SingleResult success() {
    return new SingleResult(Result.SUCCESS, null);
  }

  public static SingleResult failure(String message) {
    return new SingleResult(Result.FAILURE, message);
  }

  public boolean isSuccess() {
    return result.isSuccess();
  }

  public Optional<String> getMessage() {
    return Optional.ofNullable(message);
  }

  @RequiredArgsConstructor
  private enum Result {
    SUCCESS(true),
    FAILURE(false);

    private final boolean value;

    boolean isSuccess() {
      return value;
    }
  }
}
