import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-select-reasoning-branches',
  templateUrl: './select-reasoning-branches-form.component.html',
  styleUrls: ['./select-reasoning-branches-form.component.scss']
})
export class SelectReasoningBranchesFormComponent implements OnInit {

  translatePrefix = 'changeRequest.select.';

  constructor() {}

  ngOnInit() { }
}
