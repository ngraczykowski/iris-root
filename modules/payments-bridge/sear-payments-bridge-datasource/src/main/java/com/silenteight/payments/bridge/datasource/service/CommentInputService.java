package com.silenteight.payments.bridge.datasource.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.commentinput.CommentInputFacade;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CommentInputService {

  private final CommentInputFacade facade;
}
