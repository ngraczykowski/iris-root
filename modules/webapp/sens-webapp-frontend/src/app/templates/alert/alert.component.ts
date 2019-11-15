import { DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BreadcrumbsProvider } from '@app/components/breadcrumbs/breadcrumbs.component';
import { AlertBreadcrumbsProvider } from '@app/components/breadcrumbs/providers/alert/alert-breadcrumbs-provider';
import { TableData } from '@app/components/dynamic-table/dynamic-table.model';
import { KeyValueTableEntry } from '@app/components/key-value-vertical-table/key-value-vertical-table.component';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { AlertDetails } from '@model/alert.model';
import { tap } from 'rxjs/internal/operators';
import { map } from 'rxjs/operators';
import { AlertService } from './alert.service';
import { MatchRow, MatchTableDataMapper, matchTableFields } from './match-table-data-mapper';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.scss']
})
export class AlertComponent implements OnInit {

  private static ERROR_MAPPER = new ErrorMapper({});

  breadcrumbsProvider: BreadcrumbsProvider;

  alertDetails: AlertDetails;

  partyFields;

  currentBranchMatchTableProvider;
  otherBranchesMatchTableProvider;

  otherBranchesTableHasData = false;
  shouldDisplayHitTables;

  routeParams;

  constructor(private route: ActivatedRoute, private router: Router, private alertService: AlertService) { }

  ngOnInit() {
    this.load();
  }

  getBranchUrl(decisionTreeId, matchGroupId) {
    return '/decision-tree/' + decisionTreeId + '/reasoning-branch/' + matchGroupId;
  }

  load() {
    this.route.params.subscribe(routeParams => {
      this.routeParams = routeParams;
      this.loadDetailsAndBuildMatchesTable();
    });
  }

  goToBranch(match: MatchRow) {
    const linkToBranch = '/decision-tree/' + this.routeParams.decisionTreeId + '/reasoning-branch/' + match.matchGroupId;
    this.router.navigate([linkToBranch]);
  }

  private handleAlertDetailsLoadingError(err) {
    if (err instanceof HttpErrorResponse) {
      if (this.shouldRedirectToPageWithNoBranchSelected(err, this.routeParams)) {
        this.navigateToPageWithNoBranchSelected();
      }
    }
  }

  private navigateToPageWithNoBranchSelected() {
    this.router.navigate([
      '/decision-tree', this.routeParams.decisionTreeId, 'alert', this.routeParams.externalId
    ]);
  }

  private loadDetailsAndBuildMatchesTable() {
    this.alertService.getAlertDetails(this.routeParams)
        .subscribe(
            (alertDetails) => {
              this.partyFields = this.getPartyFields(alertDetails);
              this.alertDetails = alertDetails;
              this.breadcrumbsProvider = new AlertBreadcrumbsProvider(this.routeParams.matchGroupId, alertDetails);
              this.buildCurrentBranchMatchesTableProvider();
              this.buildOtherBranchMatchesTableProvider();
            },
            (err) => this.handleAlertDetailsLoadingError(err),
            () => this.shouldDisplayHitTables = true
        );
  }

  private buildOtherBranchMatchesTableProvider() {
    const fields = [
      matchTableFields.MATCH_GROUP_ID,
      matchTableFields.ID,
      matchTableFields.EXTERNAL_ID,
      matchTableFields.DISCRIMINATOR];
    const mapper = new MatchTableDataMapper(this.alertDetails.matchFieldNames,
        fields);

    this.otherBranchesMatchTableProvider = (page: number, size: number) =>
        this.alertService.getMatchesByAlertNotInMatchGroup(
            page, size, this.alertDetails.id, this.routeParams.matchGroupId
        ).pipe(
            map(value => mapper.map(value)),
            tap((rows: TableData) => this.otherBranchesTableHasData = rows.total > 0)
        );
  }

  private buildCurrentBranchMatchesTableProvider() {
    const fields = [
      matchTableFields.ID, matchTableFields.EXTERNAL_ID, matchTableFields.DISCRIMINATOR];
    const mapper = new MatchTableDataMapper(this.alertDetails.matchFieldNames,
        fields);

    this.currentBranchMatchTableProvider = (page: number, size: number) =>
        this.alertService.getMatchesByAlertInMatchGroup(
            page, size, this.alertDetails.id, this.routeParams.matchGroupId
        ).pipe(map(value => mapper.map(value)));
  }

  private shouldRedirectToPageWithNoBranchSelected(err: HttpErrorResponse, params) {
    return err.error && err.error.key === 'MatchGroupNotFound' && params.decisionTreeId && params.externalId;
  }

  getPartyTranslationPrefix(fieldName) {
    return 'alert.infobox.alert.party.values.' + fieldName + '.';
  }

  getFeaturesTranslationPrefix(featureName) {
    return 'branch.features.values.' + featureName + '.';
  }

  mapToKeyValueTable(solution, labelName) {
    const entries: KeyValueTableEntry[] = [];

    if (solution) {
      entries.push(
          {name: 'alert.infobox.comment.labels.' + labelName, value: solution.decision},
          {name: 'alert.infobox.comment.labels.comment', value: solution.comment}
      );
      if (solution.date) {
        entries.push(
            {name: 'alert.infobox.comment.labels.date', value: this.formatDate(solution.date)});
      }
    }
    return entries;
  }

  private formatDate(dateString) {
    const timestamp = Date.parse(dateString);
    return new DatePipe('en-US').transform(timestamp, 'yyyy-MM-dd');
  }

  private getPartyFields(alertDetails) {
    const fields = [];
    fields.push({
      'name': this.formatFieldName('Alert ', 'ID'),
      'value': alertDetails.party.externalId
    });
    alertDetails.party.fields.forEach((field) => fields.push({
      'name': this.formatFieldName('', field.name),
      'value': field.value
    }));
    return fields;
  }

  private formatFieldName(prefix: string, fieldName: string): string {
    return prefix + fieldName[0].toUpperCase() + fieldName.substr(1, fieldName.length);
  }
}
