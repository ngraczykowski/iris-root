import { Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Observable, throwError } from 'rxjs';

@Injectable()
export class TranslateServiceWrapper {

  constructor(private translate: TranslateService) { }

  get(key: string | Array<string>, interpolateParams?: Object): Observable<string | any> {
    try {
      return this.translate.get(key, interpolateParams);
    } catch (e) {
      return throwError(e);
    }
  }
}
