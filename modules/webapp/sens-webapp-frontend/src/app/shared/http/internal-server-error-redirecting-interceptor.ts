import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { RedirectorService } from '../redirector.service';
import { HttpErrorRedirectingInterceptor } from './http-error-redirecting-interceptor';

/**
 * Intercepts 5xx errors (except 502, 504) Internal Server Errors
 */
@Injectable()
export class InternalServerErrorRedirectingInterceptor extends HttpErrorRedirectingInterceptor {

  constructor(redirector: RedirectorService) {
    super(redirector, {
      urlMatchers: environment.internalServerErrorRedirectRequestMatchers,
      redirectUrl: '/500',
      statusCodePredicate: c => InternalServerErrorRedirectingInterceptor.isSupportedError(c)
    });
  }

  private static isSupportedError(status: number): boolean {
    return !this.isGatewayError(status) && this.isServerError(status);
  }

  private static isGatewayError(status: number): boolean {
    return status === 504 || status === 502;
  }

  private static isServerError(status: number): boolean {
    return status >= 500 && status < 600;
  }
}
