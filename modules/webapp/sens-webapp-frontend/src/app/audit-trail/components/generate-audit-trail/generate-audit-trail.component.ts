import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-generate-audit-trail',
  templateUrl: './generate-audit-trail.component.html',
  styleUrls: ['./generate-audit-trail.component.scss']
})
export class GenerateAuditTrailComponent implements OnInit {
  @Input() inProgress = true;
  @Input() error = false;
  @Output() abort = new EventEmitter();

  stateInProgress = {
    inProgress: true,
    title: 'auditTrail.inProgress.title',
    description: 'auditTrail.inProgress.description'
  };

  stateTimeout = {
    inProgress: false,
    title: 'auditTrail.errorTimeout.title',
    description: 'auditTrail.errorTimeout.description'
  };

  constructor() { }

  ngOnInit() {
  }

  abortClicked() {
    this.abort.emit();
  }

}
