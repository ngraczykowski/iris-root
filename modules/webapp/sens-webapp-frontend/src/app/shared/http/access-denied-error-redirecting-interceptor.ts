import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { RedirectorService } from '../redirector.service';
import { HttpErrorRedirectingInterceptor } from './http-error-redirecting-interceptor';

@Injectable()
export class AccessDeniedErrorRedirectingInterceptor extends HttpErrorRedirectingInterceptor {

  constructor(redirector: RedirectorService) {
    super(redirector, {
      urlMatchers: environment.accessDeniedErrorRedirectRequestMatchers,
      redirectUrl: '/403',
      statusCodePredicate: s => s === 403
    });
  }
}
