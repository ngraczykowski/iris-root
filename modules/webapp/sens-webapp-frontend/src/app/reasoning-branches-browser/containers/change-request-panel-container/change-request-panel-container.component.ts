import { Component, OnDestroy} from '@angular/core';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { Router } from '@angular/router';
import { CreateChangeRequestComponent } from '@app/reasoning-branches-browser/components/create-change-request/create-change-request.component';
import { ReasoningBranchesList } from '@app/reasoning-branches-browser/model/branches-list';
import { SelectedBranchesService } from '@app/reasoning-branches-browser/services/selected-branches.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-change-request-panel-container',
  templateUrl: './change-request-panel-container.component.html'
})
export class ChangeRequestPanelContainerComponent implements OnDestroy {

  showChangeRequestPanel: boolean;
  private _subscription: Subscription;

  constructor(
      private _bottomSheet: MatBottomSheet,
      private selectedBranchesService: SelectedBranchesService,
      private router: Router
  ) {
    this._subscription = selectedBranchesService.selectedReasoningBranchesCount
        .subscribe((numberOfSelectedRb) => {
              if (numberOfSelectedRb > 0) {
                this.openBottomSheet();
                this.showChangeRequestPanel = true;
              } else {
                this.closeBottomSheet();
                this.showChangeRequestPanel = false;
              }
            }
        );
  }

  ngOnDestroy() {
    this.closeBottomSheet();
    this._subscription.unsubscribe();
  }

  openBottomSheet(): void {
    if (!this.showChangeRequestPanel) {
      const bottomSheetRef = this._bottomSheet.open(CreateChangeRequestComponent,
          {
            disableClose: true,
            hasBackdrop: false
          }
      );
      bottomSheetRef.afterDismissed().subscribe((data) => {
        if (data === 'submit') {
          this.submitChangeRequest();
        }
      });
    }
  }

  closeBottomSheet(): void {
    this._bottomSheet.dismiss();
    this.showChangeRequestPanel = false;
  }

  private submitChangeRequest() {
    const selectedBranches: ReasoningBranchesList[] = this.selectedBranchesService.getSelectedBranchesList();
    const changeRequestDtId = selectedBranches[0].reasoningBranchId.decisionTreeId.toString();
    const changeRequestRbIds = [];

    selectedBranches.forEach(branch => {
      changeRequestRbIds.push(branch.reasoningBranchId.featureVectorId.toString());
    });

    this.createChangeRequest(changeRequestDtId, changeRequestRbIds.toString());
  }

  private createChangeRequest(decisionTreeId, featureVectorIds) {
    this.router.navigate(
        ['reasoning-branches/change-request'],
        {
          queryParams: {
            dt_id: decisionTreeId,
            fv_ids: featureVectorIds
          }
        });
  }
}
