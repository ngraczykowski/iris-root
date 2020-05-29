import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-selected-reasoning-branches',
  templateUrl: './selected-reasoning-branches.component.html'
})
export class SelectedReasoningBranchesComponent implements OnInit {
  @Input() decisionTreeId: number;
  @Input() reasoningBranchesCount: number;

  translatePrefix = 'changeRequest.configureForm.selectedBranches.';

  constructor() { }

  ngOnInit() {
  }

}
