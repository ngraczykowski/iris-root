import { Pipe, PipeTransform } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';
import { FriendlyValueFunction } from '../../translate/missing-key-function';
import { Translator } from '../../translate/translator';

@Pipe({
  name: 'translateOrFriendlyValue'
})
export class TranslateOrFriendlyValuePipe implements PipeTransform {

  private translator: Translator;

  constructor(service: TranslateService) {
    this.translator = new Translator(service, new FriendlyValueFunction());
  }

  transform(value, translationKeyPrefix?: string): Observable<any> {
    return this.translator.translate(value, translationKeyPrefix);
  }
}
