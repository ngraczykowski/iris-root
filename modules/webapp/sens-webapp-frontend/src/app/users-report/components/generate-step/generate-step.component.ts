import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-generate-step',
  templateUrl: './generate-step.component.html'
})
export class GenerateStepComponent implements OnInit {
  @Input() inProgress = true;
  @Input() error = false;
  @Output() abort = new EventEmitter();

  stateInProgress = {
    inProgress: true,
    title: 'usersReport.inProgress.title',
    description: 'usersReport.inProgress.description'
  };

  stateTimeout = {
    inProgress: false,
    title: 'usersReport.errorTimeout.title',
    description: 'usersReport.errorTimeout.description'
  };

  constructor() { }

  ngOnInit() {
  }

  abortClicked() {
    this.abort.emit();
  }

}
