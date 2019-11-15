import { Component, OnInit } from '@angular/core';
import {
  Label,
  Row,
  TableData
} from '@app/components/dynamic-view-table/dynamic-view-table.component';
import { View } from '@app/components/dynamic-view/dynamic-view.component';
import { filter } from 'rxjs/operators';
import { AlertRestrictionsManagementService } from '../alert-restrictions-management.service';
import { Restriction } from '../restriction';
import { AlertRestrictionTitleViewComponent } from './alerts-restrictions-views/alert-restriction-title-view/alert-restriction-title-view.component';
import { AlertsRestrictionsActionsViewComponent } from './alerts-restrictions-views/alerts-restrictions-actions-view/alerts-restrictions-actions-view.component';
import { AlertsRestrictionsDetailsViewComponent } from './alerts-restrictions-views/alerts-restrictions-details-view/alerts-restrictions-details-view.component';
import { AlertsRestrictionsNameViewComponent } from './alerts-restrictions-views/alerts-restrictions-name-view/alerts-restrictions-name-view.component';

@Component({
  selector: 'app-alerts-restrictions-table',
  templateUrl: './alerts-restrictions-table.component.html',
  styleUrls: ['./alerts-restrictions-table.component.scss']
})
export class AlertsRestrictionsTableComponent implements OnInit {
  restrictionsData: Restriction[];
  error: boolean;
  inProgress: boolean;
  tableData: TableData;

  private tableLabels = ['Name', 'Countries', 'Batch Types', 'Actions'];

  constructor(
    private alertRestrictionsService: AlertRestrictionsManagementService
  ) { }

  ngOnInit() {
    this.alertRestrictionsService.restrictions.pipe(
      filter(data => data !== null)
    ).subscribe(data => {
      this.restrictionsData = data.restrictions;
      this.tableData = {
        total: this.restrictionsData.length,
        rows: this.restrictionsData
          .map(restriction => this.getView(restriction))
          .map(views => <Row> {views: views}),
        labels: this.getTableLabels(this.tableLabels)
      };
    });

    this.alertRestrictionsService.get();
  }

  private getView(restriction: Restriction): View[] {
    const views = [];
    views.push(
      new View(AlertsRestrictionsNameViewComponent, {
        data: restriction.name
      }),
      new View(AlertsRestrictionsDetailsViewComponent, {
        data: restriction.countries,
        emptyState: 'alertRestrictionsManagement.table.data.countries.value.emptyState'
      }),
      new View(AlertsRestrictionsDetailsViewComponent, {
        data: restriction.units,
        emptyState: 'alertRestrictionsManagement.table.data.batchTypes.value.emptyState'
      }),
      new View(AlertsRestrictionsActionsViewComponent, {
        data: restriction.id
      }),
    );
    return views;
  }

  private getTableLabels(labels: string[]): Label[] {
    return labels.map(label => {
      return { view: AlertRestrictionTitleViewComponent.of(label) };
    });
  }
}
