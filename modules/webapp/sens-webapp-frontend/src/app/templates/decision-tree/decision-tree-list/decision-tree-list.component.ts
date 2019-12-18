import { Component, OnDestroy, OnInit } from '@angular/core';
import { EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import ArrayHelper from '@app/shared/helpers/array-helper';
import { ErrorMapper } from '@app/shared/http/error-mapper';
import { environment } from '@env/environment';
import { DecisionTree } from '@model/decision-tree.model';
import { Observable, of, throwError } from 'rxjs';
import { IntervalObservable } from 'rxjs/observable/IntervalObservable';
import { catchError, finalize, flatMap, startWith, tap } from 'rxjs/operators';
import { Subscription } from 'rxjs/Subscription';
import { DecisionTreeListService } from './decision-tree-list.service';

@Component({
  selector: 'app-decision-tree-list',
  templateUrl: './decision-tree-list.component.html',
  styleUrls: ['./decision-tree-list.component.scss']
})
export class DecisionTreeListComponent implements OnInit, OnDestroy {

  private static ERROR_MAPPER = new ErrorMapper({}, 'decisionTrees.message.find.error.');

  hasDecisionTreeManageAccess: Observable<boolean>;

  firstTimeLoaded: boolean;

  searchText: string;

  activeDecisionTrees: DecisionTree[];
  inactiveDecisionTrees: DecisionTree[];

  private _decisionTreesSubscription: Subscription;
  private _pollSubscription: Subscription;
  private _decisionTreesIsPending: boolean;

  constructor(
      private decisionTreesService: DecisionTreeListService,
      private eventService: LocalEventService
  ) { }

  ngOnInit() {
    this.startPollingDecisionTrees();
    this.hasDecisionTreeManageAccess = this.decisionTreesService.hasDecisionTreeManageAccess();
  }

  ngOnDestroy() {
    this.stopPollingDecisionTrees();
    this.cancelDecisionTreesSubscription();
  }

  onCreateDecisionTree() {
    this.decisionTreesService.openCreateDecisionTreeWindow();
  }

  updateDecisionTrees() {
    if (!this._decisionTreesIsPending) {
      this._decisionTreesIsPending = true;
      this.cancelDecisionTreesSubscription();
      this._decisionTreesSubscription =
          of({})
              .pipe(
                  flatMap(() => this.updateActiveDecisionTrees()),
                  flatMap(() => this.updateInactiveDecisionTrees()),
                  catchError((err) => this.handleError(err)),
                  finalize(() => this._decisionTreesIsPending = false)
              )
              .subscribe(() => this.onUpdateSuccess());
    }
  }

  private startPollingDecisionTrees() {
    this._pollSubscription =
        IntervalObservable.create(environment.decisionTrees.pollIntervalInMs)
            .pipe(startWith({}))
            .subscribe(() => this.updateDecisionTrees());
  }

  private stopPollingDecisionTrees() {
    this._pollSubscription.unsubscribe();
  }

  private onUpdateSuccess() {
    if (!this.firstTimeLoaded) {
      this.firstTimeLoaded = true;
    }
  }

  private cancelDecisionTreesSubscription() {
    if (this._decisionTreesSubscription) {
      this._decisionTreesSubscription.unsubscribe();
    }
  }

  private updateActiveDecisionTrees(): Observable<any> {
    return this.decisionTreesService.getActiveDecisionTrees()
      .pipe(tap(response => {
        if (this.checkIfTreeListChanged(this.activeDecisionTrees, response.results)) {
          this.activeDecisionTrees = response.results;
        }
      }));
  }

  private updateInactiveDecisionTrees(): Observable<any> {
    return this.decisionTreesService.getInactiveDecisionTrees()
      .pipe(tap(response => {
        if (this.checkIfTreeListChanged(this.inactiveDecisionTrees, response.results)) {
          this.inactiveDecisionTrees = response.results;
        }
      }));
  }

  private handleError(err: any): Observable<any> {
    const errorMessage = DecisionTreeListComponent.ERROR_MAPPER.get(err);
    this.sendNotificationErrorEvent(errorMessage);
    return throwError(err);
  }

  private sendNotificationErrorEvent(errorMessage) {
    this.eventService.sendEvent({
      key: EventKey.NOTIFICATION,
      data: {
        type: 'error',
        message: errorMessage
      }
    });
  }

  private checkIfTreeListChanged(existingList: DecisionTree[], newList: DecisionTree[]): boolean {
    return !ArrayHelper.compareArrays(existingList, newList, this.compareDecisionTrees);
  }

  private compareDecisionTrees(tree: DecisionTree, otherTree: DecisionTree): boolean {
    return tree.id === otherTree.id
      && tree.active === otherTree.active
      && tree.name === otherTree.name
      && (tree.status === otherTree.status ||
        (tree.status && otherTree.status && tree.status.name === otherTree.status.name))
      && ArrayHelper.compareArrays(tree.assignments, otherTree.assignments);
  }

  showDecisionTrees(decisionTrees: DecisionTree[]) {
    return decisionTrees && decisionTrees.length > 0;
  }
}
