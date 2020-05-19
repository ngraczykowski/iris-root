import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-selected-reasoning-branches',
  templateUrl: './selected-reasoning-branches.component.html',
  styleUrls: ['./selected-reasoning-branches.component.scss']
})
export class SelectedReasoningBranchesComponent implements OnInit {

  stepsPrefix = 'changeRequest.selectedBranches.';

  constructor() { }

  ngOnInit() {
  }

}
