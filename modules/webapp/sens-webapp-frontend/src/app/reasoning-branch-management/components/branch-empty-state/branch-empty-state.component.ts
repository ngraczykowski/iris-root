import { Component, Input, OnInit } from '@angular/core';
import { ReasoningBranchEmptyStates } from '@app/reasoning-branch-management/models/reasoning-branch-management';

@Component({
  selector: 'app-branch-empty-state',
  templateUrl: './branch-empty-state.component.html',
  styleUrls: ['./branch-empty-state.component.scss']
})
export class BranchEmptyStateComponent implements OnInit {

  @Input() message;
  @Input() hint;

  constructor() { }

  ngOnInit() {
  }

}
