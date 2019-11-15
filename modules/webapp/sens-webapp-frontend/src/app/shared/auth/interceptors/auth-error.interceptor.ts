import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UnauthorizedHttpUserEvent } from '@app/shared/auth/auth.http-user-event';
import { AuthService } from '@app/shared/auth/auth.service';

import { Observable, of, throwError } from 'rxjs';
import { catchError, mergeMap } from 'rxjs/operators';

/**
 * Intercepts 401 HTTP error responses returned from API and tries to handle them gracefully.
 */
@Injectable()
export class AuthErrorInterceptor implements HttpInterceptor {

  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request)
        .pipe(
          catchError(err => this.tryToHandleAuthError(err, request, next))
        );
  }

  private tryToHandleAuthError(error: any, request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (error instanceof HttpErrorResponse) {
      const response = <HttpErrorResponse>error;

      if (response.status === 401) {
        return this.handleUnauthorized(error, request, next);
      } else {
        console.warn('Unhandled HTTP error: response=%o', response);
      }
    }

    return throwError(error);
  }

  private handleUnauthorized(
      response: HttpErrorResponse,
      request: HttpRequest<any>,
      next: HttpHandler): Observable<HttpEvent<any>> {

    if (this.authService.isLoginUrl(request.url)) {
      return throwError(response);
    }

    const loginAttempt = this.authService.login();
    if (loginAttempt instanceof Observable) {
      return loginAttempt.pipe(
          mergeMap(loggedIn => {
            if (loggedIn) {
              console.log('Retrying request after logging in');
              return next.handle(request.clone());
            } else {
              console.log('Failed to log in');
              return throwError(response);
            }
          })
      );
    } else {
      return of(new UnauthorizedHttpUserEvent());
    }
  }
}
