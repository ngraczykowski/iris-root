import { Component, HostListener, Inject, OnInit, OnDestroy, ElementRef, Input } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { WINDOW } from '@app/shared/window.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-decision-tree-reports-menu',
  templateUrl: './decision-tree-reports-menu.component.html',
  styleUrls: ['./decision-tree-reports-menu.component.scss']
})
export class DecisionTreeReportsMenuComponent implements OnInit, OnDestroy {

  @Input() hasDecisionTreeViewPermission: boolean;

  showMenu: boolean;
  decisionTreeId: number;
  routeSubscription: Subscription;

  constructor(@Inject(WINDOW) public window: Window,
              private activatedRoute: ActivatedRoute,
              private eventService: LocalEventService,
              private eRef: ElementRef
  ) {}

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.showMenu = false;
    }
  }

  ngOnInit() {
    this.routeSubscription = this.activatedRoute.params.subscribe(params => {
      this.decisionTreeId = params.decisionTreeId;
    });
  }

  ngOnDestroy(): void {
    this.routeSubscription.unsubscribe();
  }

  generateReport() {
    this.window.location.assign(this.buildUrl());
    this.sendBriefMessage('decisionTree.reasoningBranches.notifications.reportGeneration');
  }

  private buildUrl(): string {
    return `/rest/webapp/api/decision-tree/${this.decisionTreeId}/circuit-breaker-triggered-alerts`;
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }

}
