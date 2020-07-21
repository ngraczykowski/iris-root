import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { environment } from '@env/environment';
import { TranslateService } from '@ngx-translate/core';
import { DialogService } from '@ui/dialog/services/dialog.service';
import { Observable, of, throwError } from 'rxjs';
import { catchError, delay, flatMap, tap } from 'rxjs/operators';

/**
 * Intercepts 502 (Bad Gateway), 504 (Gateway Timeout)
 */
@Injectable()
export class HttpTimeoutErrorInterceptor implements HttpInterceptor {

  private errorDialogRef: MatDialogRef<any>;

  constructor(private dialogService: DialogService, private translateSerivce: TranslateService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => this.handle(err, request, next)));
  }

  private handle(error: any, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (error instanceof HttpErrorResponse) {
      const response = <HttpErrorResponse>error;

      if (response.status === 502 || response.status === 504) {
        return this.handleGatewayTimeout(response, request, next);
      }
    }
    return throwError(error);
  }

  private handleGatewayTimeout(error: HttpErrorResponse, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return of({})
      .pipe(
        tap(() => {
          this.showAction(error);
        }),
        delay(environment.http.error.retryIntervalInMs),
        flatMap(() => next.handle(request.clone())),
        catchError(err => this.handle(err, request, next))
      );
  }

  private showAction(response: HttpErrorResponse) {

    if (this.errorDialogRef) {
      return;
    }

    this.errorDialogRef = this.dialogService.open({
      data: {
        header: this.translateSerivce.instant('unexpected-error.dialog.title'),
        description: this.translateSerivce.instant('unexpected-error.dialog.description')
      }
    });

    this.errorDialogRef.afterClosed().subscribe(() => {
      this.errorDialogRef = null;
    });
  }

}
