import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { TranslateServiceWrapper } from '@app/shared/translate/translate-service-wrapper';
import { from, Observable, Subscription } from 'rxjs';
import { catchError, concatMap, toArray } from 'rxjs/operators';

export interface ErrorData {
  key: string;
  extras?: any;
}

@Component({
  selector: 'app-error-feedback',
  templateUrl: './error-feedback.component.html',
  styleUrls: ['./error-feedback.component.scss']
})
export class ErrorFeedbackComponent implements OnInit, OnDestroy {

  private readonly unknownKey = 'unknown';

  @Input() translatePrefix = '';

  @Input()
  set data(errors: ErrorData | ErrorData[]) {
    if (errors) {
      this.loadErrorMessages(errors instanceof Array ? errors : [errors]);
    }
  }

  errorMessages: string[];

  private subscription: Subscription;

  constructor(private translate: TranslateServiceWrapper) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.cancelSubscription();
  }

  private loadErrorMessages(errors: ErrorData[]) {
    this.cancelSubscription();
    this.subscription = from(errors)
        .pipe(
            concatMap(e => this.getErrorMessage(e)),
            toArray()
        )
        .subscribe(errorMessages => this.errorMessages = errorMessages);
  }

  private getErrorMessage(e: ErrorData): Observable<string> {
    return this.translate.get(this.getTranslationKey(e.key), e.extras)
        .pipe(catchError(() => this.translate.get(this.getTranslationKey(this.unknownKey))));
  }

  private getTranslationKey(key: string) {
    return this.translatePrefix ? `${this.translatePrefix}.${key}` : key;
  }

  private cancelSubscription() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }
}
