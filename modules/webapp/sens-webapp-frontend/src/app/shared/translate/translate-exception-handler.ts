import { MissingTranslationHandler, MissingTranslationHandlerParams } from '@ngx-translate/core';

export class MissingTranslationException extends Error {

  constructor(params: { key }) {
    super('Translation not found "' + params.key + '".');
    Object.setPrototypeOf(this, MissingTranslationException.prototype);
  }
}

export class TranslateExceptionHandler implements MissingTranslationHandler {
  handle(params: MissingTranslationHandlerParams) {
    throw new MissingTranslationException(params);
  }
}
