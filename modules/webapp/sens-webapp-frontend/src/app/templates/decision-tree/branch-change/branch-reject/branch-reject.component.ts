import { Component, Input, OnInit } from '@angular/core';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { BranchChangeStateService } from '@app/templates/decision-tree/branch-change/branch-change-state.service';
import { BranchRejectService } from '@app/templates/decision-tree/branch-change/branch-reject/branch-reject.service';
import { BranchPageLoader } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import { InMemoryBranchPageProvider } from '@app/templates/decision-tree/branch-table/in-memory-branch-page-provider';
import { TranslateService } from '@ngx-translate/core';
import { finalize } from 'rxjs/operators';
import { BranchUpdateStateData } from '../branch-change.component';

@Component({
  selector: 'app-branch-reject',
  templateUrl: './branch-reject.component.html',
  styleUrls: ['./branch-reject.component.scss']
})
export class BranchRejectComponent implements OnInit {

  @Input()
  set data(data: BranchUpdateStateData) {
    this._data = data;
    this.refreshLoader();
  }

  get data() {
    return this._data;
  }

  private _data: BranchUpdateStateData;

  inProgress: boolean;
  error: ErrorData;
  loader: BranchPageLoader;

  constructor(
      private rejectService: BranchRejectService,
      private stateService: BranchChangeStateService,
      private eventService: LocalEventService,
      private translateService: TranslateService,
  ) { }

  ngOnInit() {
  }

  shouldDisableRejectChangesButton() {
    return !this.rejectService.isChangeRequestReady();
  }

  onRejectChanges() {
    this.rejectChanges();
  }

  onCancel() {
    this.backToPreviousStep();
  }

  private refreshLoader() {
    const provider = new InMemoryBranchPageProvider(this.data.branches, this.data.branchModel);
    this.loader = new BranchPageLoader(provider);
  }

  private rejectChanges() {
    const confimationMessage = this.translateService.instant(
        'decisionTree.branchReject.rejectChanges.confirmation');

    if (confirm(confimationMessage)) {
      this.inProgress = true;
      this.rejectService.rejectChange(this.data.decisionTreeId, this.data.branches)
          .pipe(finalize(() => this.inProgress = false))
          .subscribe(
              () => this.onSuggestChangesSuccess(),
              e => this.onSuggestChangesError(e)
          );
    }
  }

  private onSuggestChangesSuccess() {
    this.error = null;
    this.sendSuccessNotificationEvent();
    this.backToPreviousStep();
  }

  private onSuggestChangesError(e) {
    this.error = e.error;
  }

  private backToPreviousStep() {
    this.stateService.setSelectState({decisionTreeId: this.data.decisionTreeId});
  }

  private sendSuccessNotificationEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.branchReject.notification.success'
      }
    });
  }
}
