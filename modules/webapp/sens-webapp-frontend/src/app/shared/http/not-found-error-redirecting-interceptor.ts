import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { RedirectorService } from '../redirector.service';
import { HttpErrorRedirectingInterceptor } from './http-error-redirecting-interceptor';

@Injectable()
export class NotFoundErrorRedirectingInterceptor extends HttpErrorRedirectingInterceptor {

  constructor(redirector: RedirectorService) {
    super(redirector, {
      urlMatchers: environment.notFoundErrorRedirectRequestMatchers,
      redirectUrl: '/404',
      statusCodePredicate: s => s === 404
    });
  }
}
