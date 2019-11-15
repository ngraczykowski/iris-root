import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription, throwError } from 'rxjs';
import { IntervalObservable } from 'rxjs/observable/IntervalObservable';
import { finalize, startWith } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';
import { EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../shared/http/error-mapper';
import { InboxService } from '../inbox.service';

@Component({
  selector: 'app-inbox-tab',
  templateUrl: './inbox-tab.component.html',
  styleUrls: ['./inbox-tab.component.scss']
})
export class InboxTabComponent implements OnInit, OnDestroy {

  private static UNSOLVED_INBOX_MESSAGE_STATE = 'UNSOLVED';
  private static ERROR_MAPPER = new ErrorMapper({}, 'navigation.infoboxTab.error.');

  public unsolvedInboxMessageCount: number;

  private _inboxStatsIsPending: boolean;
  private _inboxStatsSubscription: Subscription;
  private _pollSubscription: Subscription;

  constructor(
      private inboxService: InboxService,
      private eventService: LocalEventService) {
  }

  ngOnInit() {
    this._pollSubscription =
        IntervalObservable.create(environment.mainNavigation.infoboxTab.pollInboxStatsInMs)
            .pipe(startWith({}))
            .subscribe(() => this.updateInboxStats());
  }

  private updateInboxStats() {
    if (!this._inboxStatsIsPending) {
      this._inboxStatsIsPending = true;
      this.cancelInboxStatsSubscription();
      this._inboxStatsSubscription = this.inboxService.getStats()
          .pipe(finalize(() => this._inboxStatsIsPending = false))
          .subscribe(
              s => this.unsolvedInboxMessageCount = s.stats[InboxTabComponent.UNSOLVED_INBOX_MESSAGE_STATE],
              error => this.handleError(error)
          );
    }
  }

  ngOnDestroy() {
    this.cancelInboxStatsSubscription();
    this._pollSubscription.unsubscribe();
  }

  private cancelInboxStatsSubscription() {
    if (this._inboxStatsSubscription) {
      this._inboxStatsSubscription.unsubscribe();
    }
  }

  private handleError(err: any): Observable<any> {
    const errorMessage = InboxTabComponent.ERROR_MAPPER.get(err);
    this.sendNotificationErrorEvent(errorMessage);
    return throwError(err);
  }

  private sendNotificationErrorEvent(errorMessage) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: errorMessage
      }
    });
  }
}
