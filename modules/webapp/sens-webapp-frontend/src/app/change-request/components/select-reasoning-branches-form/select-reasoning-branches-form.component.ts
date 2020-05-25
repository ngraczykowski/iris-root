import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-select-reasoning-branches',
  templateUrl: './select-reasoning-branches-form.component.html'
})
export class SelectReasoningBranchesFormComponent implements OnInit {

  translatePrefix = 'changeRequest.select.';

  constructor() {}

  ngOnInit() { }
}
