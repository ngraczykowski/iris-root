import { Injectable } from '@angular/core';
import { ReasoningBranchesList } from '@app/reasoning-branches-browser/model/branches-list';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SelectedBranchesService {

  selectedReasoningBranches: ReasoningBranchesList[];
  selectedReasoningBranchesCount: Subject<number> = new Subject<number>();

  constructor() { }

  updateSelectedBranchesList(list: ReasoningBranchesList[]) {
    this.selectedReasoningBranches = list;
    this.selectedReasoningBranchesCount.next(list.length);
  }

  getSelectedBranchesList() {
    return this.selectedReasoningBranches;
  }
}
