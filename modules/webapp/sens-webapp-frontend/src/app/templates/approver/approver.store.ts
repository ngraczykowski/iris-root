import { Injectable } from '@angular/core';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { TablePage } from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import { SelectableItemStore } from '@app/components/selectable-dynamic-table/selectable-item-store';
import { ApproverApiService } from '@app/templates/approver/approver-api.service';
import { ChangeRequest } from '@app/templates/approver/approver.model';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { filter, finalize, map, take } from 'rxjs/operators';

export interface ChangesQueue {
  changes: ChangeRequest[];
  total: number;
  isLoading: boolean;
  error: ErrorData;
}

export interface ApprovingPanel {
  isOpen: boolean;
  isProcessing: boolean;
  items: ChangeRequest[];
  reject: boolean;
  error: ErrorData;
}

export interface Changelog {
  isOpen: boolean;
  decisionTreeId: number;
  matchGroupId: number;
}

export interface OpenChangelogEvent {
  decisionTreeId: number;
  matchGroupId: number;
}

@Injectable()
export class ApproverStore {

  public readonly selectedApprovals = new SelectableItemStore<ChangeRequest>();
  private _changelog: BehaviorSubject<Changelog> = new BehaviorSubject<any>({
    isOpen: false,
    decisionTreeId: null,
    matchGroupId: null
  });
  public readonly changelog$ = this._changelog.asObservable();
  private _changesApprovalPanel: BehaviorSubject<ApprovingPanel> = new BehaviorSubject({
    isOpen: false,
    isProcessing: false,
    items: [],
    reject: false,
    error: null
  });
  public readonly changesApprovalPanel$ = this._changesApprovalPanel.asObservable();
  private _approvalChangesQueue: BehaviorSubject<ChangesQueue> = new BehaviorSubject({
    total: 0,
    changes: [],
    isLoading: false,
    error: null
  });
  public readonly approvalChangesQueue$ = this._approvalChangesQueue.asObservable();

  constructor(private api: ApproverApiService) {
  }

  private static calculateOffset(page: number, size: number) {
    return (page - 1) * size;
  }

  public fetchChangesQueue(): void {
    this.setChangesIsLoading(true);
    this.setChangesQueueFetchingError(null);

    this.api.fetchApprovalQueue().pipe(
        finalize(() => this.setChangesIsLoading(false))
    ).subscribe(
            (queue) => this.setChangesQueue(queue),
            (error) => this.setChangesQueueFetchingError(error),
        );
  }

  public openChangelog({decisionTreeId, matchGroupId}: OpenChangelogEvent): void {
    this.setChangelogOpen(decisionTreeId, matchGroupId);
  }

  public closeChangelog(): void {
    this.setChangelogClosed();
  }

  public openChangesApprovalPanel(approve): void {
    this.setChangesApprovalPanelOpen(this.selectedApprovals.getAll(), approve);
  }

  public closeChangesApprovalPanel(): void {
    this.setChangesApprovalPanelClosed();
    this.selectedApprovals.clear();
  }

  public getChangesToApprovePage(page: number, size: number): Observable<TablePage<ChangeRequest>> {
    const offset = ApproverStore.calculateOffset(page, size);

    return this._approvalChangesQueue.asObservable()
        .pipe(
            filter(queue => queue.isLoading === false),
            map(queue => <TablePage<ChangeRequest>>{
              items: queue.changes.slice(offset, offset + size),
              total: queue.total
            }),
            take(1)
        );
  }

  public rejectSelectedChanges(comment: string): Observable<void> {
    const completed = new Subject<void>();

    this.setApprovalPanelProcessing(true);
    this.setApprovalPanelError(null);

    this.api.rejectChanges(this._changesApprovalPanel.getValue().items, comment)
        .pipe(
            finalize(() => this.setApprovalPanelProcessing(false)),
            finalize(() => {
              completed.next();
              completed.complete();
            }),
        ).subscribe({
      error: err => {
        this.setApprovalPanelError(err);
        completed.error(err);
      }
    });

    return completed.asObservable();
  }

  public approveSelectedChanges(comment: string): Observable<void> {
    const completed = new Subject<void>();

    this.setApprovalPanelProcessing(true);
    this.setApprovalPanelError(null);

    this.api.approveChanges(this._changesApprovalPanel.getValue().items, comment)
        .pipe(
            finalize(() => this.setApprovalPanelProcessing(false)),
            finalize(() => {
              completed.next();
              completed.complete();
            }),
        ).subscribe({
      error: err => {
        this.setApprovalPanelError(err);
        completed.error(err);
      }
    });

    return completed.asObservable();
  }

  public getSelectedChangesPage(page: number, size: number): Observable<TablePage<ChangeRequest>> {
    const offset = ApproverStore.calculateOffset(page, size);

    return this._changesApprovalPanel.asObservable().pipe(
        map(approvalState => <TablePage<ChangeRequest>>{
          items: approvalState.items.slice(offset, offset + size),
          total: approvalState.items.length
        }),
        take(1)
    );
  }

  private setChangesQueueFetchingError(error) {
    this._approvalChangesQueue.next(<ChangesQueue>{
      ...this._approvalChangesQueue.getValue(),
      error
    });
  }

  private setChangesQueue(queue) {
    this._approvalChangesQueue.next({
      ...this._approvalChangesQueue.getValue(),
      changes: queue,
      total: queue.length,
    });
  }

  private setChangesIsLoading(isLoading) {
    this._approvalChangesQueue.next({
      ...this._approvalChangesQueue.getValue(),
      isLoading
    });
  }

  private setChangelogOpen(decisionTreeId: number, matchGroupId: number) {
    this._changelog.next({
      ...this._changelog.getValue(),
      isOpen: true,
      decisionTreeId,
      matchGroupId
    });
  }

  private setChangelogClosed() {
    this._changelog.next({
      ...this._changelog.getValue(),
      isOpen: false
    });
  }

  private setChangesApprovalPanelOpen(approvals, approve) {
    this._changesApprovalPanel.next({
      ...this._changesApprovalPanel.getValue(),
      isOpen: true,
      reject: !approve,
      items: approvals
    });
  }

  private setChangesApprovalPanelClosed() {
    this._changesApprovalPanel.next(<ApprovingPanel>{
      ...this._changesApprovalPanel.getValue(),
      isOpen: false,
      items: [],
    });
  }

  private setApprovalPanelProcessing(isProcessing: boolean): void {
    this._changesApprovalPanel.next({
      ...this._changesApprovalPanel.getValue(),
      isProcessing
    });
  }

  private setApprovalPanelError(error: any): void {
    this._changesApprovalPanel.next({
      ...this._changesApprovalPanel.getValue(),
      error
    });
  }

  isReject() {
    return this._changesApprovalPanel.getValue().reject;
  }
}
