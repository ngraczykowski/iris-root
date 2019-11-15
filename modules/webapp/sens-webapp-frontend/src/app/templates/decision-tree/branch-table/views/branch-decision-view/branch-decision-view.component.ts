import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface BranchDecisionViewData {
  decision: string;
}

@Component({
  selector: 'app-branch-decision-view',
  templateUrl: './branch-decision-view.component.html',
  styleUrls: ['./branch-decision-view.component.scss']
})
export class BranchDecisionViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchDecisionViewData;

  constructor() { }

  ngOnInit() {
  }

}
