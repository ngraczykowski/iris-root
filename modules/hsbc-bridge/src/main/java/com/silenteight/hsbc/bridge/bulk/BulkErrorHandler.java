package com.silenteight.hsbc.bridge.bulk;

import com.silenteight.hsbc.bridge.bulk.exception.BulkWithGivenIdAlreadyCreatedException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkIdAlreadyUsedException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BulkProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.output.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

public class BulkErrorHandler {


}
