import { Component, OnInit } from '@angular/core';
import { finalize } from 'rxjs/operators';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';
import { Event, EventKey } from '../../../../../shared/event/event.service.model';
import { LocalEventService } from '../../../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../../../shared/http/error-mapper';
import { InboxService } from '../../../inbox.service';

export interface ActionViewData {
  inboxMessageId: number;
}

@Component({
  selector: 'app-action-view',
  templateUrl: './action-view.component.html',
  styleUrls: ['./action-view.component.scss']
})
export class ActionViewComponent implements DynamicComponent, OnInit {

  private readonly solvingErrorMapper = new ErrorMapper(
      {},
      'inbox.notification.error.solve.');

  data: ActionViewData;

  solved: boolean;
  inProgress: boolean;

  constructor(private eventService: LocalEventService, private inboxService: InboxService) { }

  ngOnInit() {
  }

  solve() {
    this.inProgress = true;
    this.inboxService.markAsSolved(this.data.inboxMessageId)
        .pipe(finalize(() => this.inProgress = false))
        .subscribe(
            () => this.onSolved(),
            err => this.onError(err)
        );
  }

  private onSolved() {
    this.solved = true;
    this.sendSuccessEvent();
  }

  private onError(error) {
    this.sendErrorEvent(error);
  }

  private sendSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'inbox.notification.success.solve'
      }
    });
  }

  private sendErrorEvent(error) {
    this.eventService.sendEvent(<Event>{
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: this.solvingErrorMapper.get(error)
      }
    });
  }
}
