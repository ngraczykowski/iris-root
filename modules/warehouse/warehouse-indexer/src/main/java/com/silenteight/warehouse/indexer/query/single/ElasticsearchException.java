package com.silenteight.warehouse.indexer.query.single;

class ElasticsearchException extends RuntimeException {

  private static final long serialVersionUID = 3087594624385942465L;

  ElasticsearchException(String message, Throwable cause) {
    super(message, cause);
  }
}
