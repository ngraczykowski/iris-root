import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { environment } from '@env/environment';
import { Observable, of, throwError } from 'rxjs';
import { catchError, delay, flatMap, tap } from 'rxjs/operators';

/**
 * Intercepts 502 (Bad Gateway), 504 (Gateway Timeout)
 */
@Injectable()
export class HttpTimeoutErrorInterceptor implements HttpInterceptor {

  private show: boolean;

  constructor(private eventService: LocalEventService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => this.handle(err, request, next)));
  }

  private handle(error: any, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (error instanceof HttpErrorResponse) {
      const response = <HttpErrorResponse>error;

      if (response.status === 502 || response.status === 504) {
        return this.handleGatewayTimeout(response, request, next);
      } else if (this.show) {
        this.hideAction();
      }
    }
    return throwError(error);
  }

  private handleGatewayTimeout(error: HttpErrorResponse, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return of({})
      .pipe(
        tap(() => {
          if (!this.show) {
            this.showAction(error);
          }
        }),
        delay(environment.http.error.retryIntervalInMs),
        flatMap(() => next.handle(request.clone())),
        tap((data) => {
          if (data instanceof HttpResponse) {
            this.hideAction();
          }
        }),
        catchError(err => this.handle(err, request, next))
      );
  }

  private showAction(response: HttpErrorResponse) {
    this.show = true;
    this.eventService.sendEvent({key: EventKey.SHOW_ERROR_WINDOW, data: response});

  }

  private hideAction() {
    this.show = false;
    this.eventService.sendEvent({key: EventKey.HIDE_ERROR_WINDOW});
  }
}
