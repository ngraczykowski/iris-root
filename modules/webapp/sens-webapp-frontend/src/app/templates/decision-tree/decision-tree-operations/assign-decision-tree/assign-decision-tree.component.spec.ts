import { DebugElement } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { asyncScheduler, EMPTY, of, throwError } from 'rxjs';
import { EmptyObservable } from 'rxjs/observable/EmptyObservable';
import { TestModule } from '../../../../test/test.module';
import { AssignDecisionTreeComponent } from './assign-decision-tree.component';
import { AssignDecisionTreeModule } from './assign-decision-tree.module';
import { AssignedToStore } from './assigned-to.store';

describe('AssignDecisionTreeComponent', () => {
  let underTest: AssignDecisionTreeComponent;
  let fixture: ComponentFixture<AssignDecisionTreeComponent>;
  let store: AssignedToStore;

  let saveChangesButton: DebugElement;
  let appLoadingComponent: DebugElement;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, AssignDecisionTreeModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AssignDecisionTreeComponent);
    underTest = fixture.componentInstance;
    store = TestBed.get(AssignedToStore);

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

    expect(saveChangesButton.nativeElement.disabled).toBeTruthy();
  });

  it('should saveChangesButton be activated, when invoke makeDirty', () => {
    const givenDecisionTreeId = 1;
    spyOn(store, 'fetchDecisionTreeBatchTypes')
        .and.returnValue(EMPTY);

    underTest.open({id: givenDecisionTreeId});
    underTest.makeDirty();
    fixture.detectChanges();

    expect(saveChangesButton.nativeElement.disabled).toBeFalsy();
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

    underTest.onSaveChanges();

    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeTruthy();

    tick();

    fixture.detectChanges();
    expect(loadingComponentIsVisible()).toBeFalsy();
  }));

  function loadingComponentIsVisible() {
    return appLoadingComponent.nativeElement.classList.contains('is-active');
  }
});
