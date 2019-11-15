import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { BranchFilterClient } from '../branch-filter-client';

export interface Filter {
  id: number;
  name: string;
  query: string;
}

@Component({
  selector: 'app-saved-filters',
  templateUrl: './saved-filters.component.html',
  styleUrls: ['./saved-filters.component.scss']
})
export class SavedFiltersComponent implements OnInit, OnDestroy {

  @Input()
  set show(show: boolean) {
    this._show = show;
    if (show) {
      this.onShow();
    }
  }

  get show() {
    return this._show;
  }

  private _show;
  refreshInProgress: boolean;
  deleteInProgress: boolean;

  @Output() showChange: EventEmitter<boolean> = new EventEmitter();
  @Output() select: EventEmitter<Filter> = new EventEmitter<Filter>();

  searchText: string;
  filters: Filter[];

  private refreshSubscription: Subscription;
  private deleteSubscription: Subscription;

  constructor(private client: BranchFilterClient,
              private eventService: LocalEventService,
              private translateService: TranslateService) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.cancelRefreshTask();
    this.cancelDeleteTask();
  }

  onSelect(filter: Filter) {
    this.select.emit(filter);
    this.close();
    this.sendBriefMessage('decisionTree.branchFilter.loadFilter.briefMessage');
  }

  onDelete(filter: Filter) {
    const confimationMessage = this.translateService.instant(
        'decisionTree.branchFilter.deleteFilter.confirmDescription');

    if (confirm(confimationMessage)) {
      this.cancelDeleteTask();
      this.deleteInProgress = true;
      this.deleteSubscription = this.client.deleteFilter(filter.id)
          .pipe(finalize(() => this.deleteInProgress = false))
          .subscribe(() => this.refreshFilters());
      this.sendBriefMessage('decisionTree.branchFilter.deleteFilter.briefMessage');
    }
  }

  onClose() {
    this.close();
  }

  isLoading() {
    return this.refreshInProgress || this.deleteInProgress;
  }

  shouldShowEmptyState() {
    return this.filters && this.filters.length === 0;
  }

  shouldShowSearchBar() {
    return this.filters && this.filters.length > 0;
  }

  private refreshFilters() {
    this.cancelRefreshTask();
    this.refreshInProgress = true;
    this.refreshSubscription = this.client.getFilters()
        .pipe(finalize(() => this.refreshInProgress = false))
        .subscribe(data => this.filters = data);
  }

  private onShow() {
    this.searchText = '';
    this.refreshFilters();
  }

  private close() {
    this.show = false;
    this.showChange.emit(false);
  }

  private cancelDeleteTask() {
    if (this.deleteSubscription) {
      this.deleteSubscription.unsubscribe();
    }
  }

  private cancelRefreshTask() {
    if (this.refreshSubscription) {
      this.refreshSubscription.unsubscribe();
    }
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

