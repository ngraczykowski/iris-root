import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '../../../../../components/dynamic-view/dynamic-view.component';

export interface BranchStatusViewData {
  enabled: boolean;
}

@Component({
  selector: 'app-branch-status-view',
  templateUrl: './branch-status-view.component.html',
  styleUrls: ['./branch-status-view.component.scss']
})
export class BranchStatusViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchStatusViewData;

  constructor() { }

  ngOnInit() {
  }

}
