import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';
import { fromEvent, Observable } from 'rxjs';
import { debounceTime, distinctUntilChanged, startWith } from 'rxjs/operators';

const windowResizeEventDebounceTime: number = 200;

@Injectable({
  providedIn: 'root'
})
export class WindowService {
  readonly document: Document;

  constructor(@Inject(DOCUMENT) document) {
    this.document = document;
  }

  get nativeWindow(): Window {
    return typeof window !== 'undefined' ? window : undefined;
  }

  get sessionStorage(): Storage {
    return this.nativeWindow ? this.nativeWindow.sessionStorage : undefined;
  }

  get localStorage(): Storage {
    return this.nativeWindow ? this.nativeWindow.localStorage : undefined;
  }

  get resize(): Observable<any> {
    return fromEvent(this.nativeWindow, 'resize').pipe(
      debounceTime(windowResizeEventDebounceTime),
      startWith({ target: this.nativeWindow }),
      distinctUntilChanged()
    );
  }

}
