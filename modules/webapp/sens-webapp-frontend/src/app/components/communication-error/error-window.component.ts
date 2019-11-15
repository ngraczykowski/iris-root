import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-error-window',
  templateUrl: './error-window.component.html',
  styleUrls: ['./error-window.component.scss']
})
export class ErrorWindowComponent implements OnInit, OnDestroy {

  show: boolean;

  private subscriptions: Subscription[] = [];

  constructor(private eventService: LocalEventService) { }

  ngOnInit() {
    this.subscriptions.push(
      this.eventService.subscribe(event => {
        this.show = true;
      }, [EventKey.SHOW_ERROR_WINDOW]),
        this.eventService.subscribe(() => this.show = false, [EventKey.HIDE_ERROR_WINDOW])
    );
  }

  ngOnDestroy() {
    this.subscriptions.forEach(s => s.unsubscribe());
  }
}
