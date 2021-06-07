package com.silenteight.hsbc.bridge.watchlist;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;
import com.silenteight.hsbc.bridge.file.ResourceDoesNotExistException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.google.common.base.Strings.isNullOrEmpty;

@RestController
@RequestMapping("/watchlist/v1")
@RequiredArgsConstructor
class WatchlistController {

  private final SaveOriginalWatchlistUseCase saveWatchlistUseCase;

  @PostMapping("/upload")
  public void transferFile(
      @RequestPart("coreWatchlist") MultipartFile coreWatchlist,
      @RequestPart("nameAliasesWatchlist") MultipartFile nameAliasesWatchlist,
      @RequestPart("keywordsWatchlist") MultipartFile keywordsWatchlist,
      @RequestPart("coreChecksum") MultipartFile coreChecksum,
      @RequestPart("aliasesChecksum") MultipartFile aliasesChecksum) throws IOException {
    validateArchives(coreWatchlist, nameAliasesWatchlist);
    validateKeywordsFile(keywordsWatchlist);
    saveWatchlistUseCase.save(
        RawWatchlistData.of(
            coreWatchlist.getInputStream(), coreWatchlist.getOriginalFilename()),
        RawWatchlistData.of(
            nameAliasesWatchlist.getInputStream(), nameAliasesWatchlist.getOriginalFilename()),
        RawWatchlistData.of(
            keywordsWatchlist.getInputStream(), keywordsWatchlist.getOriginalFilename())
    );

    //TODO (smrozowski): delete original archives
    //TODO (smrozowski): verify if checksum is correct
  }

  private void validateKeywordsFile(MultipartFile file) {
    if (isNullOrEmpty(file.getOriginalFilename()))
      throw new NoFileException("No file was specified");
    if (!StringUtils.endsWith(file.getOriginalFilename(), ".xml"))
      throw new IncorrectFileExtensionException("File extension is incorrect");
  }


  private void validateArchives(MultipartFile... files) {
    for (MultipartFile a : files) {
      if (isNullOrEmpty(a.getOriginalFilename()))
        throw new NoFileException("No file was specified");
      if (!isExtensionValid(a.getOriginalFilename()))
        throw new IncorrectFileExtensionException("File extension is incorrect");
    }
  }

  private static boolean isExtensionValid(String originalFilename) {
    return StringUtils.endsWith(originalFilename, ".gz") &&
        !StringUtils.endsWith(originalFilename, "tar.gz");
  }

  @ExceptionHandler({ IOException.class })
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleExceptionWithServerErrorStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ ResourceDoesNotExistException.class, IncorrectFileExtensionException.class })
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


