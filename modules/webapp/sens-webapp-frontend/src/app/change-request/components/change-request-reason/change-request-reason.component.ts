import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-change-request-reason',
  templateUrl: './change-request-reason.component.html'
})
export class ChangeRequestReasonComponent implements OnInit {

  translatePrefix = 'changeRequest.configureForm.reason.';

  constructor() { }

  ngOnInit() {
  }

}
