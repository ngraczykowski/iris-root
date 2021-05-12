package com.silenteight.hsbc.bridge.file;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.google.common.base.Strings.isNullOrEmpty;

@RestController
@RequestMapping("/v1/upload")
@RequiredArgsConstructor
class FileTransferController {

  private final FileTransferUseCase fileTransferUseCase;

  @PostMapping
  public void transferFile(@RequestPart("file") MultipartFile file) throws IOException {
    if (isNullOrEmpty(file.getOriginalFilename()))
      throw new NoFileException("No file was specified");
    fileTransferUseCase.transfer(file.getInputStream(), file.getOriginalFilename());
  }

  @ExceptionHandler({ FileExistsException.class })
  @ResponseStatus(value = HttpStatus.CONFLICT)
  public ResponseEntity<ErrorResponse> handleExceptionWithConflictStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler({ FileTransferException.class })
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorResponse> handleExceptionWithServerErrorStatus(
      RuntimeException exception) {
    return getErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({ NoFileException.class })
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
}
