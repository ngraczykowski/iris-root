import { Component, ViewChild } from '@angular/core';
import { ReasoningBranchesListContainerComponent } from '@app/reasoning-branches-browser/containers/reasoning-branches-list-container/reasoning-branches-list-container.component';
import {
  ReasoningBranchesList,
  ReasoningBranchesListResponse
} from '@app/reasoning-branches-browser/model/branches-list';

@Component({
  selector: 'app-reasoning-branch-browser',
  templateUrl: './reasoning-branches-browser.component.html'
})
export class ReasoningBranchesBrowserComponent {

  @ViewChild(ReasoningBranchesListContainerComponent,
      {static: false}) reasoningBranchesListContainerComponent:
      ReasoningBranchesListContainerComponent;

  loadingMoreInProgress = false;

  branchDetails: ReasoningBranchesList;

  constructor() { }

  onTableScroll(e) {
    const tableViewHeight = e.target.offsetHeight;
    const tableScrollHeight = e.target.scrollHeight;
    const scrollLocation = e.target.scrollTop;

    const buffer = 1000;
    const limit = tableScrollHeight - tableViewHeight - buffer;
    if (scrollLocation > limit && !this.loadingMoreInProgress) {
      this.loadingMoreInProgress = true;
      this.reasoningBranchesListContainerComponent.loadMoreReasoningBranches();
    }
  }

  resetLazyLoader() {
    this.loadingMoreInProgress = false;
  }

  provideBranchDetails(branchDetails) {
    this.branchDetails = branchDetails;
  }

}
