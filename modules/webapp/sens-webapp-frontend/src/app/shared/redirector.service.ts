import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';

@Injectable()
export class RedirectorService {

  constructor(@Inject(DOCUMENT) private document: any) {
  }

  redirectToUrl(url: string): boolean {
    if (this.document) {
      this.document.location.assign(url);
      return true;
    } else {
      return false;
    }
  }
}
