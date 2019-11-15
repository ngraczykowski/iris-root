import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { Event, EventKey } from '@app/shared/event/event.service.model';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { TestModule } from '@app/test/test.module';
import { asyncScheduler, of, throwError } from 'rxjs';
import { tap } from 'rxjs/operators';
import { BranchFilterClient } from './branch-filter-client';
import { BranchFilterComponent } from './branch-filter.component';
import { BranchFilterModule } from './branch-filter.module';
import { Filter } from './saved-filters/saved-filters.component';

describe('BranchFilterComponent', () => {
  let component: BranchFilterComponent;
  let fixture: ComponentFixture<BranchFilterComponent>;

  let client: BranchFilterClient;
  let eventService: LocalEventService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchFilterModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    client = TestBed.get(BranchFilterClient);
    eventService = TestBed.get(LocalEventService);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should reset query and filter name on click reset', () => {
    component.query = 'query';
    component.filterName = 'filterName';

    component.onReset();

    expect(component.query).toBeFalsy();
    expect(component.filterName).toBeFalsy();
  });

  it('should emit queryChange on click reset', () => {
    spyOn(component.queryChange, 'emit');

    component.onReset();

    expect(component.queryChange.emit).toHaveBeenCalledTimes(1);
    expect(component.queryChange.emit).toHaveBeenCalledWith('');
  });

  it('should update query and filter name on select filter', () => {
    component.onSelectFilter(<Filter> {name: 'name', query: 'query'});

    expect(component.filterName).toEqual('name');
    expect(component.query).toEqual('query');
  });

  it('should showSavedFilters be true after toggle show filters button once', () => {
    component.toggleSavedFilters();

    expect(component.showSavedFilters).toBeTruthy();
  });

  it('should showSavedFilters be false after toggle show filters button twice', () => {
    component.toggleSavedFilters();
    component.toggleSavedFilters();

    expect(component.showSavedFilters).toBeFalsy();
  });

  it('should send query change event on click execute', () => {
    spyOn(component.queryChange, 'emit');

    component.query = 'query';
    component.onExecute();

    expect(component.queryChange.emit).toHaveBeenCalledTimes(1);
    expect(component.queryChange.emit).toHaveBeenCalledWith('query');
  });

  it('should not send query change event when only changed query text', () => {
    spyOn(component.queryChange, 'emit');

    component.query = 'query';

    expect(component.queryChange.emit).toHaveBeenCalledTimes(0);
  });

  it('should set executeErrorMessage when got executeError', () => {
    component.executeError = {};

    expect(component.queryErrors).toEqual(['decisionTree.branchFilter.error.UNKNOWN']);
  });

  it('should reset executeErrorMessage when got null executeError', () => {
    component.executeError = null;

    expect(component.queryErrors).toEqual([]);
  });

  it('should set saveInProgress onSave', fakeAsync(() => {
    spyOn(client, 'saveFilter')
        .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

    component.onSave();

    expect(component.saveInProgress).toBeTruthy();
    tick(50);
    expect(component.saveInProgress).toBeTruthy();
    tick(50);
    expect(component.saveInProgress).toBeFalsy();
  }));

  it('should set saveErrorMessage onSave with error', () => {
    spyOn(client, 'saveFilter').and.returnValue(throwError({}));

    component.onSave();

    expect(component.saveErrorMessage).toEqual('decisionTree.branchFilter.error.UNKNOWN');
  });

  it('should reset saveErrorMessage onSave with success', () => {
    component.saveErrorMessage = 'error';
    spyOn(client, 'saveFilter').and.returnValue(of({}));

    component.onSave();

    expect(component.saveErrorMessage).toBeFalsy();
  });

  it('should send success event onSave with success', () => {
    spyOn(client, 'saveFilter').and.returnValue(of({}));
    spyOn(eventService, 'sendEvent');

    component.onSave();

    expect(eventService.sendEvent).toHaveBeenCalledWith(<Event>{
      key: EventKey.NOTIFICATION,
      data: {
        type: 'success',
        message: 'decisionTree.branchFilter.save.notification.success'
      }
    });
  });

  it('should call saveFilter with valid filter name and query onSave', () => {
    spyOn(client, 'saveFilter').and.returnValue(of({}));
    component.filterName = 'name';
    component.query = 'query';

    component.onSave();

    expect(client.saveFilter).toHaveBeenCalledWith('name', 'query');
  });

  it('handleFilterQuery should set query and filter name if needed', () => {
    component.handleFilterQuery('test', '');
    expect(component.query).toBe('test');
    expect(component.filterName).toBe('');
  });
});
