import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface BranchScoreViewData {
  score: number;
}

@Component({
  selector: 'app-branch-score-view',
  templateUrl: './branch-score-view.component.html',
  styleUrls: ['./branch-score-view.component.scss']
})
export class BranchScoreViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchScoreViewData;

  constructor() { }

  ngOnInit() {
  }

}
