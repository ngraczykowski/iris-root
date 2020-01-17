import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BreadcrumbsProvider } from '@app/components/breadcrumbs/breadcrumbs.component';
import { DecisionTreeBreadcrumbsProvider } from '@app/components/breadcrumbs/providers/decision-tree/decision-tree-breadcrumbs-provider';
import { DecisionTreeDetails, DecisionTreePermission } from '@model/decision-tree.model';
import { Subscription } from 'rxjs';
import { DecisionTreeDetailsService } from './decision-tree-details.service';

@Component({
  selector: 'app-decision-tree-details',
  templateUrl: './decision-tree-details.component.html',
  styleUrls: ['./decision-tree-details.component.scss']
})
export class DecisionTreeDetailsComponent implements OnInit, OnDestroy {
  hasDecisionTreeViewAccess: boolean;
  hasDecisionTreeChangeAccess: boolean;

  breadcrumbsProvider: BreadcrumbsProvider;

  decisionTreeDetails: DecisionTreeDetails;

  decisionTreeId: number;

  private refreshDetailsSubscription: Subscription;
  private goBackSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private decisionTreeService: DecisionTreeDetailsService
  ) {}

  ngOnInit() {
    this.load();
    this.startListeners();
  }

  ngOnDestroy() {
    this.stopListeners();
  }

  private load() {
    this.route.params.subscribe(routeParams => {
      this.decisionTreeId = routeParams.decisionTreeId;
      this.loadDetails();
    });
  }

  private loadDetails() {
    this.decisionTreeService
      .getDecisionTreeDetails(this.decisionTreeId)
      .subscribe(decisionTree => this.onLoadDetailsSuccess(decisionTree));
  }

  private onLoadDetailsSuccess(decisionTreeDetails: DecisionTreeDetails) {
    this.decisionTreeDetails = decisionTreeDetails;
    this.breadcrumbsProvider = new DecisionTreeBreadcrumbsProvider(
      this.decisionTreeDetails
    );
    this.hasDecisionTreeViewAccess = this.hasDecisionTreeViewPermission();
    this.hasDecisionTreeChangeAccess = this.hasDecisionTreeChangePermission();
  }

  private hasDecisionTreeChangePermission() {
    // TODO(mmastylo): execute separate call for permissions
    return false;
    // return this.decisionTreeDetails.permissions.indexOf(DecisionTreePermission.DECISION_TREE_CHANGE) !== -1;
  }

  private hasDecisionTreeViewPermission() {
    // TODO(mmastylo): execute separate call for permissions
    return false;
    // return this.decisionTreeDetails.permissions.indexOf(DecisionTreePermission.DECISION_TREE_VIEW) !== -1;
  }

  private goBack() {
    this.router.navigate(['../'], { relativeTo: this.route });
  }

  private startListeners() {
    this.refreshDetailsSubscription = this.decisionTreeService
      .observeRefreshingOperationEvents()
      .subscribe(() => this.loadDetails());
    this.goBackSubscription = this.decisionTreeService
      .observeGoBackOperationEvents()
      .subscribe(() => this.goBack());
  }

  private stopListeners() {
    this.refreshDetailsSubscription.unsubscribe();
    this.goBackSubscription.unsubscribe();
  }
}
