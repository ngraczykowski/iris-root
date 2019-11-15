import { Component, Input, OnInit } from '@angular/core';
import { Branch, BranchModel } from '../../model/branch.model';
import { BranchChangeStateService } from './branch-change-state.service';

export const enum BranchChangeStateType {
  SELECT = 'SELECT',
  UPDATE = 'UPDATE',
  RESET = 'RESET'
}

export interface BranchChangeState {
  type: BranchChangeStateType;
  data: any;
}

export class BranchUpdateStateData {
  decisionTreeId: number;
  branchModel: BranchModel;
  branches: Branch[];
}

export class BranchSelectStateData {
  decisionTreeId: number;
}

@Component({
  selector: 'app-branch-change',
  templateUrl: './branch-change.component.html',
  styleUrls: ['./branch-change.component.scss']
})
export class BranchChangeComponent implements OnInit {

  @Input() decisionTreeId: number;

  state: BranchChangeState;

  constructor(private branchChangeStateService: BranchChangeStateService) { }

  ngOnInit() {
    this.branchChangeStateService.observeChanges().subscribe(state => this.state = state);
    this.branchChangeStateService.setSelectState({decisionTreeId: this.decisionTreeId});
  }
}
