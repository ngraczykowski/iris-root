import { TranslateService } from '@ngx-translate/core';
import { Observable, of, throwError } from 'rxjs';
import { MissingKeyFunction } from './missing-key-function';
import { MissingTranslationException } from './translate-exception-handler';

export class Translator {

  private static DEFAULT_MISSING_KEY_FUNCTION: MissingKeyFunction = key => of(key);
  constructor(
    private service: TranslateService,
    private missingKeyFunction: MissingKeyFunction = Translator.DEFAULT_MISSING_KEY_FUNCTION) {
    this.service.setDefaultLang('en');
    this.service.use('en');
  }

  private static isNullOrUndefined(value): boolean {
    return value === null || value === undefined;
  }

  translate(value, keyPrefix = ''): Observable<any> {
    return this.translateWithParam(value, keyPrefix, null);
  }

  translateWithParam(value, keyPrefix = '', params?: Object): Observable<any> {
    if (Translator.isNullOrUndefined(value)) {
      return of(value);
    }

    try {
      return this.service.get(keyPrefix + value, params);
    } catch (e) {
      if (e instanceof MissingTranslationException) {
        return this.missingKeyFunction.apply(value);
      }
      return throwError(e);
    }
  }
}
