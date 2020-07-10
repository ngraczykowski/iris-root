import { ChangeDetectorRef, Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatSelectChange } from '@angular/material/select';
import {
  ReasoningBranchesList,
  ReasoningBranchesListRequest
} from '@app/reasoning-branches-browser/model/branches-list';
import { ReasoningBranchesListService } from '@app/reasoning-branches-browser/services/reasoning-branches-list.service';
import { SelectedBranchesService } from '@app/reasoning-branches-browser/services/selected-branches.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { Authority } from '@app/core/authorities/model/authority.enum';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';
import { environment } from '@env/environment';

@Component({
  selector: 'app-reasoning-branches-list-container',
  templateUrl: './reasoning-branches-list-container.component.html',
  styleUrls: ['./reasoning-branches-list-container.component.scss']
})
export class ReasoningBranchesListContainerComponent implements OnInit {

  @Output() unlockLazyLoader = new EventEmitter<any>();
  @Output() branchDetails = new EventEmitter<any>();

  translatePrefix = 'reasoningBranchesBrowser.';
  loadingTranslatePrefix = this.translatePrefix + 'loading.';
  errorTranslatePrefix = this.translatePrefix + 'error.';

  showTable = false;
  showLoading = true;
  showError = false;
  showLoadMore = false;

  canSelect: boolean = this.authenticatedUserFacade.hasRole(Authority.BUSINESS_OPERATOR);

  header: Header = {
    title: this.translatePrefix + 'title'
  };

  stateLoading: StateContent = {
    centered: true,
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  stateError: StateContent = {
    centered: true,
    title: this.errorTranslatePrefix + 'title',
    description: this.errorTranslatePrefix + 'description',
    button: this.errorTranslatePrefix + 'button'
  };

  stateLoadMore: StateContent = {
    title: this.loadingTranslatePrefix + 'title',
    inProgress: true
  };

  rowsPerPage = 50;

  reasoningBranchesRequest: ReasoningBranchesListRequest = {
    pageIndex: 0,
    pageSize: this.rowsPerPage
  };

  aiSolutions = environment.aiSolutions;

  branchesList: ReasoningBranchesList[] = [];

  constructor(
      private reasoningBranchesListService: ReasoningBranchesListService,
      private selectedBranchesService: SelectedBranchesService,
      private cdr: ChangeDetectorRef,
      private readonly authenticatedUserFacade: AuthenticatedUserFacade
  ) { }

  ngOnInit() {
    this.loadReasoningBranchesList(this.reasoningBranchesRequest);
  }

  loadReasoningBranchesList(request) {
    this.reasoningBranchesListService.getReasoningBranchesList(request)
        .subscribe((data: any) => {
          if (data.total > this.branchesList.length) {
            this.branchesList = [...this.branchesList, ...data.branches];
            this.cdr.detectChanges();
            this.resetView();
            this.showTable = true;
            this.unlockLazyLoader.emit();
            if (data.branches.length > 0) {
              this.updateReasoningBranchRequest();
            }
          } else {
            this.showLoadMore = false;
          }
        }, error => {
          this.resetView();
          this.showError = true;
        });
  }

  loadMoreReasoningBranches() {
    this.showLoadMore = true;
    this.loadReasoningBranchesList(this.reasoningBranchesRequest);
  }

  private updateReasoningBranchRequest() {
    this.reasoningBranchesRequest.pageIndex++;
  }

  reset() {
    this.reasoningBranchesRequest.pageIndex = 0;
    this.reasoningBranchesRequest.pageSize = this.rowsPerPage;
    this.branchesList = [];
  }

  resetView() {
    this.showTable = false;
    this.showLoading = false;
    this.showError = false;
    this.showLoadMore = false;
  }

  updateSelectedBranchesList($event: ReasoningBranchesList[]) {
    this.selectedBranchesService.updateSelectedBranchesList($event);
  }

  showBranchDetails(branchDetails) {
    this.branchDetails.emit(branchDetails);
  }

  filterAiSolution($event: MatSelectChange) {
    if ($event.value === 'all') {
      delete this.reasoningBranchesRequest.aiSolution;
      this.loadReasoningBranchesList(this.reasoningBranchesRequest);
    } else {
      this.reasoningBranchesRequest.aiSolution = $event.value;
      this.loadReasoningBranchesList(this.reasoningBranchesRequest);
    }
  }

  filterAiStatus($event: MatSelectChange) {
    if ($event.value === 'all') {
      delete this.reasoningBranchesRequest.active;
      this.loadReasoningBranchesList(this.reasoningBranchesRequest);
    } else {
      this.reasoningBranchesRequest.active = $event.value;
      this.loadReasoningBranchesList(this.reasoningBranchesRequest);
    }
  }

  branchesFiltering($event: MatSelectChange, type) {
    this.resetView();
    this.showLoading = true;
    this.reset();
    if (type === 'solution') {
      this.filterAiSolution($event);
    } else {
      this.filterAiStatus($event);
    }
  }
}
