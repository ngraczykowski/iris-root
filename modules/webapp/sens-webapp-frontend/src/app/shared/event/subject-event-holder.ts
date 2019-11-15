import { Event, EventKey } from '@app/shared/event/event.service.model';
import { Observable, Subject } from 'rxjs';
import { filter } from 'rxjs/operators';

export class SubjectEventHolder {
  private subject: Subject<Event> = new Subject();

  publish(event: Event) {
    this.subject.next(event);
  }

  observe(keyFilters?: Array<EventKey>): Observable<Event> {
    if (keyFilters) {
      return this.subject.asObservable().pipe(
          filter(event => keyFilters.includes(event.key))
      );
    } else {
      return this.subject.asObservable();
    }
  }
}
