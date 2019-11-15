import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface BranchIdViewData {
  decisionTreeId: number;
  matchGroupId: number;
}

@Component({
  selector: 'app-branch-id-view',
  templateUrl: './branch-id-view.component.html',
  styleUrls: ['./branch-id-view.component.scss']
})
export class BranchIdViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchIdViewData;

  branchLink: string;

  constructor() {

  }

  ngOnInit() {
    if (this.data) {
      this.branchLink =
          `/decision-tree/${this.data.decisionTreeId}/reasoning-branch/${this.data.matchGroupId}`;
    }
  }
}
