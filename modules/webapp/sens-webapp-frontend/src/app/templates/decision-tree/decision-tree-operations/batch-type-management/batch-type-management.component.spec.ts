import { DebugElement } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { SharedModule } from '@app/shared/shared.module';
import { asyncScheduler, EMPTY, of, throwError } from 'rxjs';
import { EmptyObservable } from 'rxjs/observable/EmptyObservable';
import { TestModule } from '../../../../test/test.module';
import { BatchTypeManagementComponent } from './batch-type-management.component';
import { BatchTypeManagementModule } from './batch-type-management.module';
import { BatchTypeManagementStore } from './batch-type-management.store';
import { BatchTypeManagementService } from './batch-type-management.service';

describe('BatchTypeManagementComponent', () => {
  let underTest: BatchTypeManagementComponent;
  let fixture: ComponentFixture<BatchTypeManagementComponent>;
  let store: BatchTypeManagementStore;
  let batchTypeManagementService: BatchTypeManagementService;
  let saveChangesButton: DebugElement;
  let appLoadingComponent: DebugElement;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, BatchTypeManagementModule, SharedModule],
          providers: [BatchTypeManagementService]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BatchTypeManagementComponent);
    underTest = fixture.componentInstance;
    store = TestBed.get(BatchTypeManagementStore);
    batchTypeManagementService = TestBed.get(BatchTypeManagementService);
    fixture.detectChanges();
    saveChangesButton = fixture.debugElement.query(By.css('#saveButton'));
    appLoadingComponent = fixture.debugElement.query(By.css('app-loading'));
  });

  it('should create', () => {
    expect(underTest).toBeTruthy();
  });

  it('should call store fetch method with given tree id, when open invoked', () => {
    const givenDecisionTreeId = 1;
    spyOn(store, 'fetchDecisionTreeBatchTypes')
        .and.returnValue(new EmptyObservable());

    underTest.open({id: givenDecisionTreeId});

    expect(store.fetchDecisionTreeBatchTypes).toHaveBeenCalledWith(givenDecisionTreeId);
  });

  it('should fire output event, when saveChanges success ', fakeAsync(() => {
    const events = [];
    spyOn(store, 'saveBatchTypesAssignments')
        .and.returnValue(EMPTY);

    underTest.edit.subscribe(event => events.push(event));
    underTest.onSaveChanges();
    tick();

    expect(events.length).toBe(1);
  }));

  it('should not fire output event, when saveChanges fail ', fakeAsync(() => {
    const events = [];
    spyOn(store, 'saveBatchTypesAssignments')
        .and.returnValue(throwError({}));

    underTest.edit.subscribe(event => events.push(event));
    underTest.onSaveChanges();
    tick();

    expect(events.length).toBe(0);
  }));

  it('should saveChangesButton be initially inactive', () => {
    const givenDecisionTreeId = 1;
    spyOn(store, 'fetchDecisionTreeBatchTypes')
        .and.returnValue(EMPTY);

    underTest.open({id: givenDecisionTreeId});
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('#saveButton')).nativeElement.disabled).toBeTruthy();
  });

  it('should saveChangesButton be activated, when invoke makeDirty', () => {
    const givenDecisionTreeId = 1;
    spyOn(store, 'fetchDecisionTreeBatchTypes')
        .and.returnValue(EMPTY);

    underTest.open({id: givenDecisionTreeId});
    underTest.makeDirty();
    fixture.detectChanges();

    expect(fixture.debugElement.query(By.css('#saveButton')).nativeElement.disabled).toBeFalsy();
  });

  it('should open and close loading window, when open', fakeAsync(() => {
    const givenDecisionTreeId = 1;
    spyOn(store, 'fetchDecisionTreeBatchTypes')
        .and.returnValue(of({}, asyncScheduler));

    underTest.open({id: givenDecisionTreeId});
    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeTruthy();

    tick();

    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeFalsy();
  }));

  it('should invoke assignedToService#saveBatchTypesAssignments, when onSaveChanges called', () => {
    spyOn(store, 'saveBatchTypesAssignments')
        .and.returnValue(EMPTY);

    underTest.onSaveChanges();

    expect(store.saveBatchTypesAssignments).toHaveBeenCalled();
  });

  it('should open and close loading window, when onSaveChanges fails', fakeAsync(() => {
    spyOn(store, 'saveBatchTypesAssignments')
        .and.returnValue(throwError({}, asyncScheduler));
    underTest.show = true;
    underTest.onSaveChanges();
    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeTruthy();

    tick();
    fixture.detectChanges();

    tick();
    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeFalsy();
  }));

  it('should call BatchTypeManagementService onDiscardChanges when discard changes button is clicked', () => {
    const spy = spyOn(batchTypeManagementService, 'onDiscardChanges');
    underTest.close();
    expect(spy).toHaveBeenCalled();
    expect(spy).toHaveBeenCalledTimes(1);
  });

  function loadingComponentIsVisible() {
    return fixture.debugElement.query(By.css('app-loading')).nativeElement.classList.contains('is-active');
  }
});
