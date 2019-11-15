import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { RedirectorService } from '../redirector.service';
import { PathExtractor } from './path-extractor';
import { RequestMatcher } from './request-matcher';

export interface HttpErrorRedirectingInterceptorConfig {
  urlMatchers: UrlMatcherConfig[];
  statusCodePredicate: (number) => boolean;
  redirectUrl: string;
}

export interface UrlMatcherConfig {
  urlRegex: RegExp;
  httpMethods: string[];
}

export class HttpErrorRedirectingInterceptor implements HttpInterceptor {

  private readonly pathExtractor: PathExtractor = new PathExtractor();
  private readonly requestMatchers: RequestMatcher[];

  constructor(
      private redirector: RedirectorService,
      private config: HttpErrorRedirectingInterceptorConfig
  ) {
    this.requestMatchers = [];
    this.config.urlMatchers.forEach(m => {
      this.requestMatchers.push(new RequestMatcher(m.urlRegex, m.httpMethods));
    });
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(catchError(err => this.handle(request, err)));
  }

  private handle(request: HttpRequest<any>, error: any): Observable<HttpEvent<any>> {
    if (error instanceof HttpErrorResponse) {
      if (this.isStatusCodeMatched(error) && this.isRequestMatched(request)) {
        this.redirectToErrorPage();
      }
    }
    return throwError(error);
  }

  private isRequestMatched(request: HttpRequest<any>) {
    return this.requestMatchers.some(matcher => {
      const path: string = this.pathExtractor.removeApiUrl(request.url);
      return matcher.matches(path, request.method);
    });
  }

  private redirectToErrorPage() {
    this.redirector.redirectToUrl(this.config.redirectUrl);
  }

  private isStatusCodeMatched(response: HttpErrorResponse) {
    return this.config.statusCodePredicate(response.status);
  }
}
