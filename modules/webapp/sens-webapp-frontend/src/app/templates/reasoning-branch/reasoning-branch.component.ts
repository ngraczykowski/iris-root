import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BreadcrumbsProvider } from '@app/components/breadcrumbs/breadcrumbs.component';
import { BranchBreadcrumbsProvider } from '@app/components/breadcrumbs/providers/branch/branch-breadcrumbs-provider';
import { DynamicComponentProvider } from '@app/components/dynamic-table/dynamic-table.component';
import { HintFeedbackInput } from '@app/components/hint-feedback/hint-feedback.component';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TranslateServiceWrapper } from '@app/shared/translate/translate-service-wrapper';
import { WINDOW } from '@app/shared/window.service';
import { BranchDetails } from '@app/templates/model/branch.model';
import { Subscription } from 'rxjs';
import {
  AlertRow,
  FullAlertTableDataMapper,
  SolutionsAlertTableDataMapper
} from './alert-table-data-mapper';
import { ReasoningBranchService } from './reasoning-branch.service';

export interface QueryParamsPaging {
  suspendingTablePage: number;
  learningQualityTablePage: number;
  otherTablePage: number;
}

@Component({
  selector: 'app-reasoning-branch',
  templateUrl: './reasoning-branch.component.html',
  styleUrls: ['./reasoning-branch.component.scss']
})
export class ReasoningBranchComponent implements OnInit, OnDestroy {
  public queryParamsPages: QueryParamsPaging = {
    suspendingTablePage: 1,
    learningQualityTablePage: 1,
    otherTablePage: 1
  };

  private solutionAlertTableDataMapper = new SolutionsAlertTableDataMapper();
  private fullAlertTableDataMapper = new FullAlertTableDataMapper();
  private subscriptions: Array<Subscription> = [];

  breadcrumbsProvider: BreadcrumbsProvider;

  query;

  filteringSorting = false;
  showSolutions = false;

  branchDetails: BranchDetails;

  learningQualityAlertsDataProvider;
  otherAlertsDataProvider;

  decisionTreeId;
  matchGroupId;

  cbWarningMessageSettings: HintFeedbackInput;

  constructor(
      private route: ActivatedRoute,
      private router: Router,
      private branchService: ReasoningBranchService,
      private eventService: LocalEventService,
      private translate: TranslateServiceWrapper,
      @Inject(WINDOW) public window: Window,
  ) { }

  ngOnInit() {
    this.load();
  }

  ngOnDestroy() {
    this.subscriptions.forEach(value => value.unsubscribe());
  }

  selectAlert(alert: AlertRow) {
    const decisionTreeId = this.branchDetails.decisionTreeInfo.id;
    const matchGroupid = this.branchDetails.matchGroupId;
    const linkToAlert = '/decision-tree/' + decisionTreeId + '/reasoning-branch/' + matchGroupid + '/alert/' + alert.externalId;
    this.router.navigate([linkToAlert]);
  }

  onExecuteQuery(query) {
    this.query = query;
    this.buildTableProviders();
  }

  onShowSolutionsClick() {
    this.showSolutions = !this.showSolutions;
    this.buildTableProviders();
  }

  refreshDetails() {
    this.subscriptions.push(
        this.route.params.subscribe(params => {
          this.loadDetails();
        })
    );
  }

  load() {
    this.subscriptions.push(
        this.route.params.subscribe(params => {
          this.decisionTreeId = params.decisionTreeId;
          this.matchGroupId = params.matchGroupId;
          this.loadQueryParams();
          this.loadDetails();
          this.buildTableProviders();
        })
    );
  }

  loadQueryParams() {
    this.subscriptions.push(
        this.route.queryParams.subscribe(queryParams => {
          this.queryParamsPages.learningQualityTablePage = queryParams.learningQualityPage ?
              parseInt(queryParams.learningQualityPage, 0) : 1;
          this.queryParamsPages.otherTablePage = queryParams.otherPage ? parseInt(queryParams.otherPage, 0) : 1;
          this.queryParamsPages.suspendingTablePage = queryParams.suspendingPage ? parseInt(queryParams.suspendingPage, 0) : 1;
        })
    );
  }

  createDataProvider(filters?): DynamicComponentProvider {
    return (page: number, size: number) => {
      return this.branchService.getAlertPage(page, size, filters)
          .map(alertPage => this.chooseMapper().map(alertPage));
    };
  }

  updateUrlPagination($event, tableName) {
    const queryParams = {};
    queryParams[tableName] = $event;
    this.router.navigate([], {queryParams: queryParams, queryParamsHandling: 'merge'});
  }

  private buildTableProviders() {
    this.learningQualityAlertsDataProvider = this.createDataProvider({
      decisionTreeId: this.decisionTreeId,
      matchGroupId: this.matchGroupId,
      category: 'LEARNING',
      query: this.query
    });
    this.otherAlertsDataProvider = this.createDataProvider({
      decisionTreeId: this.decisionTreeId,
      matchGroupId: this.matchGroupId,
      category: 'OTHER',
      query: this.query
    });
  }

  private sendAlertIgnoredSuccessfulEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'reasoningBranch.alerts.table.suspension.ignoredAlertNotification'
      }
    });
  }

  private chooseMapper() {
    return (this.showSolutions ? this.solutionAlertTableDataMapper : this.fullAlertTableDataMapper);
  }

  getFeaturesTranslationPrefix(featureName) {
    return 'branch.features.values.' + featureName + '.';
  }

  private loadDetails() {
    this.branchService.getBranchDetails(this.decisionTreeId, this.matchGroupId)
        .subscribe(branchDetails => {
          this.branchDetails = branchDetails;
          this.breadcrumbsProvider = new BranchBreadcrumbsProvider(branchDetails);
          this.configureCircuitBreakerWarningMessageSettings();
        });
  }

  private configureCircuitBreakerWarningMessageSettings() {
    this.cbWarningMessageSettings = {
      type: 'negative',
      visible: this.branchDetails.disabledByCircuitBreaker,
      options: []
    };
    this.subscriptions.push(this.translate.get('reasoningBranch.cb-hint.title').subscribe(value => {
      this.cbWarningMessageSettings.title = value;
    }));
    this.subscriptions.push(this.translate.get('reasoningBranch.cb-hint.description')
        .subscribe(value => {
          this.cbWarningMessageSettings.descriptionPrimary = value;
        }));
    this.subscriptions.push(this.translate.get('reasoningBranch.cb-hint.secondary-description')
        .subscribe(value => {
          this.cbWarningMessageSettings.descriptionSecondary = value;
        }));
  }

  generateCircuitBreakerReport() {
    this.window.location.assign(this.buildCircuitBreakerReportUrl());
    this.sendBriefMessage('decisionTree.reasoningBranches.notifications.reportGeneration');
  }

  private buildCircuitBreakerReportUrl(): string {
    return `/api/decision-tree/${this.decisionTreeId}/circuit-breaker-triggered-alerts`;
  }

  private sendBriefMessage(messageContent) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        message: messageContent
      }
    });
  }
}
