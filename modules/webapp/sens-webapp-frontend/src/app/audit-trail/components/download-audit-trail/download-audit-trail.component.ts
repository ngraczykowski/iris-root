import { Component, OnInit, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-download-audit-trail',
  templateUrl: './download-audit-trail.component.html'
})
export class DownloadAuditTrailComponent implements OnInit {
  @Output() resetClicked = new EventEmitter();

  stateDownload = {
    title: 'auditTrail.download.title',
    description: 'auditTrail.download.description'
  };

  constructor() { }

  ngOnInit() {
  }

  reset() {
    this.resetClicked.emit();
  }

}
