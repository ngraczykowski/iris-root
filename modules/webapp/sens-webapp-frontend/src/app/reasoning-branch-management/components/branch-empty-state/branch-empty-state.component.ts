import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-branch-empty-state',
  templateUrl: './branch-empty-state.component.html',
  styleUrls: ['./branch-empty-state.component.scss']
})
export class BranchEmptyStateComponent implements OnInit {

  @Input() emptyStateMessage;

  constructor() { }

  ngOnInit() {
  }

}
