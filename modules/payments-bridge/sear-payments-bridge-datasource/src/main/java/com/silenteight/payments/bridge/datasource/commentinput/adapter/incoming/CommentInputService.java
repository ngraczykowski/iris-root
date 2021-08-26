package com.silenteight.payments.bridge.datasource.commentinput.adapter.incoming;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.commentinput.service.CommentInputFacade;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CommentInputService {

  private final CommentInputFacade facade;
}
