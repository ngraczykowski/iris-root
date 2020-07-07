import { ChangeDetectorRef, Component, OnDestroy } from '@angular/core';
import { MatBottomSheetRef } from '@angular/material/bottom-sheet';
import { SelectedBranchesService } from '@app/reasoning-branches-browser/services/selected-branches.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-create-change-request',
  templateUrl: './create-change-request.component.html',
  styleUrls: ['./create-change-request.component.scss']
})
export class CreateChangeRequestComponent implements OnDestroy {

  count = 1;
  private _subscription: Subscription;

  constructor(private _bottomSheetRef: MatBottomSheetRef<CreateChangeRequestComponent>,
              private selectedBranchesService: SelectedBranchesService,
              private changeDetectorRef: ChangeDetectorRef) {
    this._subscription = selectedBranchesService.selectedReasoningBranchesCount.subscribe((value) => {
      this.count = value;
      this.changeDetectorRef.detectChanges();
    });
  }

  ngOnDestroy() {
    this._subscription.unsubscribe();
  }

  submitChangeRequest(event: MouseEvent): void {
    this._bottomSheetRef.dismiss('submit');
    event.preventDefault();
  }

}
