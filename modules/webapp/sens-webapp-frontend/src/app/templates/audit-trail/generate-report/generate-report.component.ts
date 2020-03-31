import { HttpClient } from '@angular/common/http';
import { Component, Inject, Input, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { WINDOW } from '@app/shared/window.service';
import * as FileSaver from 'file-saver';

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
    private http: HttpClient,
    private eventService: LocalEventService
  ) { }

  ngOnInit() {
  }

  onGenerateReport() {
    this.http.get(this.reportUrl, { responseType: 'text' })
      .subscribe(res => {
        const blob = new Blob([res], { type: 'text/csv' });
        FileSaver.saveAs(blob, 'security-matrix-report.csv');
        this.sendBriefMessage('auditTrail.generateReport.ready.briefMessage');
      }, () => this.showErrorNotification());
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }

  private showErrorNotification() {
    this.eventService.sendEvent({
      key: EventKey.SHOW_ERROR_WINDOW
    });
  }
}
