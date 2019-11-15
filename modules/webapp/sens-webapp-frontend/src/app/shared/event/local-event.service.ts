import { Injectable } from '@angular/core';
import { Event, EventKey, EventService } from '@app/shared/event/event.service.model';
import { SubjectEventHolder } from '@app/shared/event/subject-event-holder';
import { Subscription } from 'rxjs';

@Injectable()
export class LocalEventService implements EventService {
  private events = new SubjectEventHolder();

  sendEvent(event: Event) {
    this.events.publish(event);
  }

  subscribe(eventHandler: (ev: Event) => void, keyFilters?: Array<EventKey>): Subscription {
    const events$ = this.events.observe(keyFilters);
    return events$.subscribe((e) => eventHandler(e));
  }
}
