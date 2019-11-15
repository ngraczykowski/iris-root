import { Type } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { ErrorData } from '@app/components/error-feedback/error-feedback.component';
import { Solution } from '@app/components/solution-tag/solution-tag.component';
import { EventKey, EventService } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { BranchChangeStateService } from '@app/templates/decision-tree/branch-change/branch-change-state.service';
import { ChangeRequestCommentOptionComponent } from '@app/templates/decision-tree/branch-change/branch-reject/change-request-reject-form/change-request-comment-option/change-request-comment-option.component';
import { RejectChangeRequestClient } from '@app/templates/decision-tree/branch-change/branch-reject/reject-change-request-client.service';
import { ChangeRequest } from '@app/templates/decision-tree/branch-change/branch-reject/reject-request.model';
import { Branch } from '@app/templates/model/branch.model';
import { TestModule } from '@app/test/test.module';
import { asyncScheduler, BehaviorSubject, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { BranchChangeModule } from '../branch-change.module';

import { BranchRejectComponent } from './branch-reject.component';

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

describe('BranchRejectComponent', () => {

  let component: BranchRejectComponent;
  let fixture: ComponentFixture<BranchRejectComponent>;

  let stateService: BranchChangeStateService;
  let rejectRequestClient: RejectChangeRequestClient;
  let eventService: EventService;

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
    rejectRequestClient = TestBed.get(RejectChangeRequestClient);
    eventService = TestBed.get(LocalEventService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchRejectComponent);
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

      it('should init form empty comment', () => {
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
        spyOn(rejectRequestClient, 'rejectChange').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        setComment('comment');
        component.onRejectChanges();

        expect(rejectRequestClient.rejectChange).toHaveBeenCalledWith(<ChangeRequest> {
          decisionTreeId: 1,
          matchGroupIds: [1, 2],
          comment: 'comment'
        });
      });

      it('should set select state after save success', () => {
        spyOn(rejectRequestClient, 'rejectChange').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        spyOn(stateService, 'setSelectState');

        component.onRejectChanges();

        expect(stateService.setSelectState).toHaveBeenCalledWith({decisionTreeId: 1});
      });

      it('should send success message after save success', () => {
        spyOn(rejectRequestClient, 'rejectChange').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        spyOn(eventService, 'sendEvent');

        component.onRejectChanges();

        expect(eventService.sendEvent).toHaveBeenCalledWith({
          key: EventKey.NOTIFICATION,
          data: {
            type: 'success',
            message: 'decisionTree.branchReject.notification.success'
          }
        });
      });

      it('should reset error after save success', () => {
        spyOn(rejectRequestClient, 'rejectChange').and.returnValue(of({}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });
        component.error = <ErrorData>{key: 'key'};

        component.onRejectChanges();

        expect(component.error).toBeFalsy();
      });

      it('should set error after save error', () => {
        const error = <ErrorData> {key: 'key', extras: 'extra'};
        spyOn(rejectRequestClient, 'rejectChange')
            .and.returnValue(throwError({error: error}));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        component.onRejectChanges();

        expect(component.error).toEqual(error);
      });

      it('should set loading while saving', fakeAsync(() => {
        spyOn(rejectRequestClient, 'rejectChange')
            .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));
        spyOn(window, 'confirm').and.callFake(function () { return true; });

        component.onRejectChanges();

        expect(component.inProgress).toBeTruthy();
        tick(50);
        expect(component.inProgress).toBeTruthy();
        tick(50);
        expect(component.inProgress).toBeFalsy();
      }));
    });

    describe('Disable save button test', () => {

      it('should disable save button if form is untouched', () => {
        expect(component.shouldDisableRejectChangesButton()).toBeTruthy();
      });

      it('should enable save button if there is solution change and comment', () => {
        setComment('comment');

        expect(component.shouldDisableRejectChangesButton()).toBeFalsy();
      });

    });
  });

  function getComment(): string {
    return getComponent(ChangeRequestCommentOptionComponent).control.value;
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
