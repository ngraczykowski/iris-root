import { Injectable } from '@angular/core';
import { fromEvent, Observable } from 'rxjs';
import { debounceTime, shareReplay } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class WindowService {

  private resize: Observable<Event> = fromEvent(window, 'resize').pipe(
      debounceTime(200),
      shareReplay({ bufferSize: 1, refCount: true })
  );

  constructor() { }

  public getResize(): Observable<Event> {
    return this.resize;
  }
}
