import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { asyncScheduler, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { TestModule } from '../../../../test/test.module';
import { BranchFilterClient } from '../branch-filter-client';
import { BranchFilterModule } from '../branch-filter.module';
import { Filter, SavedFiltersComponent } from './saved-filters.component';

describe('SavedFiltersComponent', () => {
  let component: SavedFiltersComponent;
  let fixture: ComponentFixture<SavedFiltersComponent>;

  let client: BranchFilterClient;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchFilterModule
          ]
        }).compileComponents();

    client = TestBed.get(BranchFilterClient);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SavedFiltersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load filters on show', () => {
    const filters = [<Filter> {name: 'filter'}];
    spyOn(client, 'getFilters').and.returnValue(of(filters));

    component.show = true;

    expect(component.filters).toEqual(filters);
  });

  it('should reset searchText on show', () => {
    component.searchText = 'text';

    component.show = true;

    expect(component.searchText).toBeFalsy();
  });

  it('should isLoading return true when loading filters', fakeAsync(() => {
    spyOn(client, 'getFilters')
        .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));

    component.show = true;

    expect(component.isLoading()).toBeTruthy();
    tick(50);
    expect(component.isLoading()).toBeTruthy();
    tick(50);
    expect(component.isLoading()).toBeFalsy();
  }));

  it('should isLoading return true when delete filters', fakeAsync(() => {
    spyOn(client, 'getFilters').and.returnValue(of({}));
    spyOn(client, 'deleteFilter')
        .and.returnValue(of({}, asyncScheduler).pipe(tap(() => tick(100))));
    spyOn(window, 'confirm').and.callFake(function () { return true; });

    component.onDelete(<Filter> {});

    expect(component.isLoading()).toBeTruthy();
    tick(50);
    expect(component.isLoading()).toBeTruthy();
    tick(50);
    expect(component.isLoading()).toBeFalsy();
  }));

  it('should refresh filters on delete one', () => {
    const filters = [<Filter> {name: 'filter'}];
    spyOn(client, 'getFilters').and.returnValue(of(filters));
    spyOn(client, 'deleteFilter').and.returnValue(of({}));
    spyOn(window, 'confirm').and.callFake(function () { return true; });

    component.onDelete(<Filter> {});

    expect(component.filters).toEqual(filters);
  });

  it('should call delete with valid id on client when delete filter', () => {
    spyOn(client, 'deleteFilter').and.returnValue(of({}));
    spyOn(window, 'confirm').and.callFake(function () { return true; });

    component.onDelete(<Filter> {id: 1});

    expect(client.deleteFilter).toHaveBeenCalledWith(1);
  });

  it('should shouldShowEmptyState return false if filters have not been loaded', () => {
    component.filters = undefined;

    expect(component.shouldShowEmptyState()).toBeFalsy();
  });

  it('should shouldShowEmptyState return true if filters are empty', () => {
    component.filters = [];

    expect(component.shouldShowEmptyState()).toBeTruthy();
  });

  it('should shouldShowEmptyState return false if there are some filters', () => {
    component.filters = [<Filter>{}, <Filter>{}];

    expect(component.shouldShowEmptyState()).toBeFalsy();
  });

  it('should shouldShowSearchBar return false if filters have not been loaded', () => {
    component.filters = undefined;

    expect(component.shouldShowSearchBar()).toBeFalsy();
  });

  it('should shouldShowSearchBar return false if filters are empty', () => {
    component.filters = [];

    expect(component.shouldShowSearchBar()).toBeFalsy();
  });

  it('should shouldShowSearchBar return true if there are some filters', () => {
    component.filters = [<Filter>{}, <Filter>{}];

    expect(component.shouldShowSearchBar()).toBeTruthy();
  });

  it('should set show false when close', () => {
    component.show = true;
    spyOn(component.showChange, 'emit');

    component.onClose();

    expect(component.show).toBeFalsy();
    expect(component.showChange.emit).toHaveBeenCalledTimes(1);
    expect(component.showChange.emit).toHaveBeenCalledWith(false);
  });

  it('should emit select event when select filter', () => {
    const filter = <Filter> {name: 'filter'};
    spyOn(component.select, 'emit');

    component.onSelect(filter);

    expect(component.select.emit).toHaveBeenCalledTimes(1);
    expect(component.select.emit).toHaveBeenCalledWith(filter);
  });
});
