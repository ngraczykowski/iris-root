import { Component, Inject, Input, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { WINDOW } from '@app/shared/window.service';

@Component({
  selector: 'app-generate-report',
  templateUrl: './generate-report.component.html',
  styleUrls: ['./generate-report.component.scss']
})
export class GenerateReportComponent implements OnInit {

  @Input() reportType;
  @Input() reportUrl;
  @Input() reportDescription;

  inProgress = false;
  jobDone = false;

  constructor(
    @Inject(WINDOW) public window: Window,
    private eventService: LocalEventService
  ) { }

  ngOnInit() {
  }

  onGenerateReport() {
    this.window.location.assign(this.reportUrl);
    this.sendBriefMessage('auditTrail.generateReport.ready.briefMessage');
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
