import { Component, OnInit } from '@angular/core';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-change-request',
  templateUrl: './change-request.component.html',
  styleUrls: ['./change-request.component.scss']
})
export class ChangeRequestComponent implements OnInit {

  stepsPrefix = 'changeRequest.steps.';

  changesConfirmation: StateContent = {
    inProgress: false,
    image: null,
    title: 'changeRequest.confirmation.title',
    description: 'changeRequest.confirmation.description'
  };

  constructor() { }

  ngOnInit() {
  }

}
