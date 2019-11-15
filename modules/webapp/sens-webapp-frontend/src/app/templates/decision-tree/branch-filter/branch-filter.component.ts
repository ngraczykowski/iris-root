import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, finalize, take } from 'rxjs/operators';
import { EventKey } from '../../../shared/event/event.service.model';
import { LocalEventService } from '../../../shared/event/local-event.service';
import { ErrorMapper } from '../../../shared/http/error-mapper';
import { BranchFilterClient } from './branch-filter-client';
import { Filter } from './saved-filters/saved-filters.component';

@Component({
  selector: 'app-branch-filter',
  templateUrl: './branch-filter.component.html',
  styleUrls: ['./branch-filter.component.scss']
})
export class BranchFilterComponent implements OnInit, OnDestroy {

  private static ERROR_MAPPER = new ErrorMapper({
    'InvalidReasoningBranchQuery': 'INVALID_QUERY',
    'UnknownFeatureName': 'UNKNOWN_FEATURE'
  }, 'decisionTree.branchFilter.error.');

  @Input()
  set executeError(executeError) {
    if (executeError) {
      this.clearErrors();
      this.queryErrors.push(BranchFilterComponent.ERROR_MAPPER.get(executeError));
    } else {
      this.clearErrors();
    }
  }

  @Input() query: string;
  @Output() queryChange: EventEmitter<string> = new EventEmitter();

  showSavedFilters = false;
  saveInProgress: boolean;

  queryErrors: string[];
  saveErrorMessage: string;

  filterName: string;

  private urlQuerySubscription: Subscription;
  private saveSubscription: Subscription;

  constructor(
      private filterClient: BranchFilterClient,
      private eventService: LocalEventService,
      private route: ActivatedRoute,
      private router: Router
  ) { }

  ngOnInit(): void {
    this.urlQuerySubscription =
        this.route.queryParams.pipe(
            filter(data => data.filterQuery),
            take(1)
        )
            .subscribe(data => this.handleFilterQuery(data.filterQuery, data.filterName ? data.filterName : ''));
  }

  ngOnDestroy() {
    this.cancelSaveTask();
  }

  onExecute() {
    this.sendQueryChangeEvent();
    this.setQueryInUrl();
  }

  onSave() {
    this.cancelSaveTask();
    this.saveInProgress = true;
    this.saveSubscription = this.filterClient.saveFilter(this.filterName, this.query)
        .pipe(finalize(() => this.saveInProgress = false))
        .subscribe(
            () => this.onSaveSuccess(),
            error => this.onSaveError(error)
        );
  }

  onReset() {
    this.reset();
    this.clearErrors();
    this.sendQueryChangeEvent();
    this.setQueryInUrl();
  }

  onSelectFilter(filterObject: Filter) {
    this.filterName = filterObject.name;
    this.query = filterObject.query;
  }

  toggleSavedFilters() {
    this.showSavedFilters = !this.showSavedFilters;
  }

  setQueryInUrl() {
    this.router.navigate([], {
      queryParamsHandling: 'merge',
      queryParams: {filterQuery: this.query, filterName: this.filterName}
    });
  }

  handleFilterQuery(query: string, queryName?: string): void {
    this.query = query;
    this.filterName = queryName;
    this.sendQueryChangeEvent();
  }

  private reset() {
    this.filterName = '';
    this.query = '';
  }

  private sendQueryChangeEvent() {
    this.queryChange.emit(this.query);
  }

  private cancelSaveTask() {
    if (this.saveSubscription) {
      this.saveSubscription.unsubscribe();
    }
  }

  private onSaveSuccess() {
    this.clearErrors();
    this.sendSuccessEvent();
  }

  private clearErrors() {
    this.queryErrors = [];
    this.saveErrorMessage = '';
  }

  private sendSuccessEvent() {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.branchFilter.save.notification.success'
      }
    });
  }

  private onSaveError(error) {
    this.saveErrorMessage = BranchFilterComponent.ERROR_MAPPER.get(error);
  }
}
