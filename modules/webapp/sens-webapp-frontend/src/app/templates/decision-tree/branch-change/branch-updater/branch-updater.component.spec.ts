import { Type } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { Solution } from '@app/components/solution-tag/solution-tag.component';
import { EventKey, EventService } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { BranchChangeStateService } from '@app/templates/decision-tree/branch-change/branch-change-state.service';
import { ChangeRequestCommentOptionComponent } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-comment-option/change-request-comment-option.component';
import { ChangeRequestSolutionOptionComponent } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-solution-option/change-request-solution-option.component';
import { ChangeRequestStatusOptionComponent } from '@app/templates/decision-tree/branch-change/branch-updater/change-request-edit-form/change-request-status-option/change-request-status-option.component';
import {
  ChangeRequest,
  StatusChange
} from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';
import { CreateChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-updater/create-change-request-client.service';
import { Branch } from '@app/templates/model/branch.model';
import { TestModule } from '@app/test/test.module';
import { asyncScheduler, BehaviorSubject, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { BranchChangeModule } from '../branch-change.module';

import { BranchUpdaterComponent } from './branch-updater.component';

class MockSolutionSettingsService {
  settings = new BehaviorSubject<Solution[]>([
    {
      key: 'solution1',
      label: 'test',
      className: 'test'
    },
    {
      key: 'solution2',
      label: 'test',
      className: 'test'
    }
  ]);
}

describe('BranchUpdaterComponent', () => {

  let component: BranchUpdaterComponent;
  let fixture: ComponentFixture<BranchUpdaterComponent>;

  let stateService: BranchChangeStateService;
  let createRequestClient: CreateChangeRequestClient;
  let eventService: EventService;

  const NONE_SOLUTION = 'NONE';
  const PTP_SOLUTION = 'POTENTIAL_TRUE_POSITIVE';

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchChangeModule
          ],
          providers: [{ provide: SolutionSettingsService, useClass: MockSolutionSettingsService }]
        })
        .compileComponents();

    stateService = TestBed.get(BranchChangeStateService);
    createRequestClient = TestBed.get(CreateChangeRequestClient);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchUpdaterComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  describe('Given component initialized with sample data', () => {

    beforeEach(() => {
      component.data = {
        decisionTreeId: 1,
        branches: [
          <Branch>{
            decisionTreeInfo: {id: 1, name: 'tree1'},
            solution: 'solution1',
            matchGroupId: 1,
            features: [{name: 'feature', value: 'value1'}]
          },
          <Branch>{
            decisionTreeInfo: {id: 1, name: 'tree1'},
            solution: 'solution2',
            matchGroupId: 2,
            features: [{name: 'feature', value: 'value2'}]
          }
        ],
        branchModel: {featureNames: ['feature']}
      };

      fixture.detectChanges();
    });

    it('should set select state after onCancel', () => {
      spyOn(stateService, 'setSelectState');

      component.onCancel();

      expect(stateService.setSelectState).toHaveBeenCalledWith({decisionTreeId: 1});
    });

    describe('Form control values test', () => {

      it('should init form with none change and empty comment', () => {
        expect(getStatus()).toEqual(NONE_SOLUTION);
        expect(getSolution()).toEqual(NONE_SOLUTION);
        expect(getComment()).toBeFalsy();
      });

      it('should reset form after onCancel', () => {
        setStatus(StatusChange.ENABLED);
        setSolution(PTP_SOLUTION);
        setComment('comment');

        component.onReset();

        expect(getStatus()).toEqual(NONE_SOLUTION);
        expect(getSolution()).toEqual(NONE_SOLUTION);
        expect(getComment()).toBeFalsy();
      });
    });

    describe('Loader test', () => {

      it('should init loader after set data', () => {
        expect(component.loader).toBeTruthy();
      });

      it('should loader return valid page data', done => {
        component.loader.load(1, 5).subscribe(data => {
          expect(data.total).toEqual(2);
          expect(data.model).toEqual(component.data.branchModel);
          expect(data.items).toEqual(component.data.branches);
          done();
        });
      });

      it('should refresh loader after refresh data', done => {
        component.data = {
          decisionTreeId: 2,
          branches: [],
          branchModel: {featureNames: []}
        };

        component.loader.load(1, 5).subscribe(data => {
          expect(data.total).toEqual(0);
          expect(data.model).toEqual({featureNames: []});
          expect(data.items).toEqual([]);
          done();
        });
      });
    });

    describe('Save changes test', () => {
      it('should create valid changes after set form', () => {
        spyOn(createRequestClient, 'createChanges').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        setSolution(PTP_SOLUTION);
        setStatus(StatusChange.ENABLED);
        setComment('comment');
        component.onSuggestChanges();

        expect(createRequestClient.createChanges).toHaveBeenCalledWith(<ChangeRequest> {
          decisionTreeId: 1,
          matchGroupIds: [1, 2],
          solution: PTP_SOLUTION,
          status: StatusChange.ENABLED,
          comment: 'comment'
        });
      });

      it('should set select state after save success', () => {
        spyOn(createRequestClient, 'createChanges').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        spyOn(stateService, 'setSelectState');

        component.onSuggestChanges();

        expect(stateService.setSelectState).toHaveBeenCalledWith({decisionTreeId: 1});
      });

      it('should send success message after save success', () => {
        spyOn(createRequestClient, 'createChanges').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        spyOn(eventService, 'sendEvent');

        component.onSuggestChanges();

        expect(eventService.sendEvent).toHaveBeenCalledWith({
          key: EventKey.NOTIFICATION,
          data: {
            type: 'success',
            message: 'decisionTree.branchUpdater.notification.success'
          }
        });
      });

      it('should reset error after save success', () => {
        spyOn(createRequestClient, 'createChanges').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        component.error = <ErrorData>{key: 'key'};

        component.onSuggestChanges();

        expect(component.error).toBeFalsy();
      });

      it('should set error after save error', () => {
        const error = <ErrorData> {key: 'key', extras: 'extra'};
        spyOn(createRequestClient, 'createChanges')
            .and.returnValue(throwError({error: error}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        component.onSuggestChanges();

        expect(component.error).toEqual(error);
      });

      it('should set loading while saving', fakeAsync(() => {
        spyOn(createRequestClient, 'createChanges')
            .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        component.onSuggestChanges();

        expect(component.inProgress).toBeTruthy();
        tick(50);
        expect(component.inProgress).toBeTruthy();
        tick(50);
        expect(component.inProgress).toBeFalsy();
      }));
    });

    describe('Disable save button test', () => {

      it('should disable save button if form is untouched', () => {
        expect(component.shouldDisableSuggestChangesButton()).toBeTruthy();
      });

      it('should disable save button if there is no comment', () => {
        setStatus(StatusChange.ENABLED);
        setSolution('POTENTIAL_TRUE_POSITIVE');

        expect(component.shouldDisableSuggestChangesButton()).toBeTruthy();
      });

      it('should enable save button if there is status change and comment', () => {
        setStatus(StatusChange.ENABLED);
        setComment('comment');

        expect(component.shouldDisableSuggestChangesButton()).toBeFalsy();
      });

      it('should enable save button if there is solution change and comment', () => {
        setSolution(PTP_SOLUTION);
        setComment('comment');

        expect(component.shouldDisableSuggestChangesButton()).toBeFalsy();
      });

      it('should disable save button if there are no changes', () => {
        setSolution(NONE_SOLUTION);
        setStatus(StatusChange.NONE);

        expect(component.shouldDisableSuggestChangesButton()).toBeTruthy();
      });
    });
  });

  function getStatus(): StatusChange {
    return getComponent(ChangeRequestStatusOptionComponent).control.value;
  }

  function getSolution() {
    return getComponent(ChangeRequestSolutionOptionComponent).control.value;
  }

  function getComment(): string {
    return getComponent(ChangeRequestCommentOptionComponent).control.value;
  }

  function setStatus(status: StatusChange) {
    const control = getComponent(ChangeRequestStatusOptionComponent).control;
    control.setValue(status);
    control.markAsDirty();
  }

  function setSolution(solution) {
    const control = getComponent(ChangeRequestSolutionOptionComponent).control;
    control.setValue(solution);
    control.markAsDirty();
  }

  function setComment(comment: string) {
    const control = getComponent(ChangeRequestCommentOptionComponent).control;
    control.setValue(comment);
    control.markAsDirty();
  }

  function getComponent<T>(type: Type<T>): T {
    fixture.detectChanges();
    return <T> fixture.debugElement.query(By.directive(type)).componentInstance;
  }
});
