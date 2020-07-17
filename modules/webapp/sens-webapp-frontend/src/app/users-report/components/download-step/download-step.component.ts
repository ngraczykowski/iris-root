import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-download-step',
  templateUrl: './download-step.component.html'
})
export class DownloadStepComponent implements OnInit {
  @Output() resetClicked = new EventEmitter();

  stateDownload = {
    title: 'usersReport.download.title',
    description: 'usersReport.download.description'
  };

  constructor() { }

  ngOnInit() {
  }

  reset() {
    this.resetClicked.emit();
  }

}
