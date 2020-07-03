import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

@Injectable()
export class HttpInternalServerErrorInterceptor implements HttpInterceptor {
  constructor(private eventService: LocalEventService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => this.handle(err, request, next)));
  }

  private handle(error: any, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (error instanceof HttpErrorResponse) {
      const response = <HttpErrorResponse>error;
      if (response.status === 500) {
        this.eventService.sendEvent({key: EventKey.SHOW_ERROR_WINDOW, data: response});
        setTimeout(() => {
          this.eventService.sendEvent({key: EventKey.HIDE_ERROR_WINDOW});
        }, 10 * 1000);
      }
    }
    return throwError(error);
  }
}
