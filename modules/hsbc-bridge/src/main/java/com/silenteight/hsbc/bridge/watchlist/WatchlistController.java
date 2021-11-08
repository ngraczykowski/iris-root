package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.watchlist.event.ZipFileWatchlistSavedEvent;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/watchlist/v1")
@RequiredArgsConstructor
class WatchlistController {

  private final WatchlistSaver watchlistSaver;
  private final ApplicationEventPublisher eventPublisher;

  @PostMapping("/upload")
  public void transferFile(MultipartRequest request) throws IOException {
    var entry = request.getFileMap().entrySet().iterator().next();
    var zipFile = entry.getValue();

    log.info("Received watchlist file with parameter name '{}'", entry.getKey());
    validateZipFile(zipFile);
    var zipUri = watchlistSaver.save(zipFile.getInputStream(), zipFile.getOriginalFilename());
    log.info("Watchlist file {} saved", zipFile.getOriginalFilename());

    eventPublisher.publishEvent(new ZipFileWatchlistSavedEvent(zipUri.toString()));
    //TODO (smrozowski): delete original archives
    //TODO (smrozowski): verify if checksum is correct
  }

  private void validateZipFile(MultipartFile file) {
    if (Strings.isNullOrEmpty(file.getOriginalFilename()))
      throw new NoFileException("No file was specified");
    if (!StringUtils.endsWith(file.getOriginalFilename(), ".zip"))
      throw new IncorrectFileExtensionException("File extension is incorrect");
  }

  @ExceptionHandler({ IOException.class })
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleExceptionWithServerErrorStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({
      LoadingException.class,
      WatchlistSaver.WatchlistSavingException.class,
      IncorrectFileExtensionException.class })
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorResponse> handleExceptionWithBadRequestStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST);
  }

  private ResponseEntity<ErrorResponse> getErrorResponse(String message, HttpStatus httpStatus) {
    return new ResponseEntity<>(
        ErrorResponse
            .builder()
            .error(httpStatus.getReasonPhrase())
            .message(message)
            .status(httpStatus.value())
            .timestamp(LocalDateTime.now())
            .build(),
        httpStatus);
  }

  static class NoFileException extends RuntimeException {

    private static final long serialVersionUID = -2205619377389853485L;

    public NoFileException(String cause) {
      super(cause);
    }
  }
}


