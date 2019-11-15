import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface BranchLabelViewData {
  text: string;
}

@Component({
  selector: 'app-branch-label-view',
  templateUrl: './branch-label-view.component.html',
  styleUrls: ['./branch-label-view.component.scss']
})
export class BranchLabelViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchLabelViewData;

  constructor() { }

  ngOnInit() {
  }

}
