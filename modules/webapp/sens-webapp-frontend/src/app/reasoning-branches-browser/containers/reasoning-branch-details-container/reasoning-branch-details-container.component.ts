import { Component, Input, OnChanges } from '@angular/core';
import { ReasoningBranchesList } from '@app/reasoning-branches-browser/model/branches-list';
import { ReasoningBranchDetailsService } from '@app/reasoning-branches-browser/services/reasoning-branch-details.service';
import { Header } from '@app/ui-components/header/header';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-reasoning-branch-details-container',
  templateUrl: './reasoning-branch-details-container.component.html'
})
export class ReasoningBranchDetailsContainerComponent implements OnChanges {

  @Input() branchDetails: ReasoningBranchesList;

  translatePrefix = 'reasoningBranchesBrowser.';
  detailsTranslatePrefix = this.translatePrefix + 'details.';

  header: Header = {
    title: this.detailsTranslatePrefix + 'title',
    parameter: ''
  };

  featuresErrorState: StateContent = {
    title: this.detailsTranslatePrefix + 'featuresLoadingErrorState.title',
    description: this.detailsTranslatePrefix + 'featuresLoadingErrorState.description',
    button: this.detailsTranslatePrefix + 'featuresLoadingErrorState.button',
  };

  featuresLoadingState: StateContent = {
    inProgress: true,
    centered: true
  };

  rbId: string;
  featuresList: [];

  showFeaturesLoading = true;
  showFeaturesError = false;
  showFeaturesList = false;

  constructor(
      private reasoningBranchDetailsService: ReasoningBranchDetailsService,
  ) { }

  ngOnChanges(changes) {
    if (this.branchDetails && changes.branchDetails) {
      this.rbId = this.generateRbId(this.branchDetails.reasoningBranchId);
      this.header.parameter = this.rbId;
      this.populateFeatureList();
    }
  }

  generateRbId(branchId) {
    return `${branchId.decisionTreeId}-${branchId.featureVectorId}`;
  }

  populateFeatureList() {
    this.resetView();
    this.showFeaturesLoading = true;
    this.reasoningBranchDetailsService.getFeaturesList(
        this.branchDetails.reasoningBranchId.decisionTreeId,
        this.branchDetails.reasoningBranchId.featureVectorId
    )
        .subscribe(data => {
          this.featuresList = data;
          this.resetView();
          this.showFeaturesList = true;
        }, error => {
          this.resetView();
          this.showFeaturesError = true;
        });
  }

  resetView() {
    this.showFeaturesError = false;
    this.showFeaturesLoading = false;
    this.showFeaturesList = false;
  }
}
