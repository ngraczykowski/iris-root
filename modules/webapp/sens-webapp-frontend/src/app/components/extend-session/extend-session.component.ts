import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { InterAppEventService } from '@app/shared/event/inter-app-event.service';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Subscription } from 'rxjs/Subscription';
import { TimeFormatter } from './time-formatter';
import { Timer } from './timer';

@Component({
  selector: 'app-extend-session',
  templateUrl: './extend-session.component.html',
  styleUrls: ['./extend-session.component.scss'],
  providers: [{provide: Timer, useValue: new Timer(500)}, TimeFormatter]
})
export class ExtendSessionComponent implements OnInit, OnDestroy {

  show = false;
  timeFormatted;

  private subscriptions: Array<Subscription> = [];

  constructor(private eventService: LocalEventService,
              private interAppEventService: InterAppEventService,
              private timer: Timer,
              private timeFormatter: TimeFormatter) { }

  ngOnInit() {
    this.subscriptions.push(this.eventService.subscribe(
        (event) => this.showNotification(event.data.expirationTime), [EventKey.SESSION_CLOSE_TO_EXPIRE]
    ));
    this.subscriptions.push(this.eventService.subscribe(
        () => this.extend(), [EventKey.ACTIVITY]
    ));
    this.subscriptions.push(this.interAppEventService.subscribe(
        () => this.extend(), [EventKey.OTHER_CONTEXT_ACTIVITY]
    ));
  }

  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.timer.cancel();
  }

  showNotification(expirationTime) {
    this.show = true;
    this.timer.start(expirationTime, timeLeft => this.timeFormatted = this.timeFormatter.format(timeLeft), () => this.show = false);
  }

  extend() {
    this.eventService.sendEvent({key: EventKey.EXTEND_SESSION});
    this.timer.cancel();
    this.show = false;
  }
}
