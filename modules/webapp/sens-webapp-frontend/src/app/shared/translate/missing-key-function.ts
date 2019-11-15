import { Observable, of } from 'rxjs';
import { FriendlyTextFormatter } from '../formatters/friendly/friendly-text-formatter';

export interface MissingKeyFunction {
  apply(key): Observable<any>;
}

export class ReturnKeyFunction implements MissingKeyFunction {
  apply(value): Observable<any> {
    return of(value);
  }
}

export class FriendlyValueFunction implements MissingKeyFunction {

  private FriendlyTextFormatter = new FriendlyTextFormatter();

  apply(value): Observable<any> {
    return of(this.FriendlyTextFormatter.apply(value));
  }
}
