import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../test/test.module';
import { UserManagementClient } from '../user-management-client';
import { UserTableComponent } from './user-table.component';
import { LocalEventService } from '@app/shared/event/local-event.service';
import { StoreModule, combineReducers } from '@ngrx/store';
import * as fromRoot from '@app/reducers';
import { UserViewModule } from './views/user-view.module';
import { UserTableDataMapper, UserTableDataProvider } from './user-table-data-provider';
import { PageableDynamicTableModule } from '@app/components/pageable-dynamic-table/pageable-dynamic-table.module';

describe('UserTableComponent', () => {
  let component: UserTableComponent;
  let fixture: ComponentFixture<UserTableComponent>;
  let localEventService: LocalEventService;
  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            UserViewModule,
            PageableDynamicTableModule,
            StoreModule.forRoot({
              ...fromRoot.reducers,
              feature: combineReducers(fromRoot.reducers)
            })
          ],
          providers: [
            UserManagementClient,
            LocalEventService,
            UserTableDataMapper,
            UserTableDataProvider
          ],
          declarations: [
            UserTableComponent
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserTableComponent);
    localEventService = TestBed.get(LocalEventService);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call LocalEventService on ngOnInit()', () => {
    const localEventServiceSpy = spyOn(localEventService, 'subscribe');
    component.ngOnInit();
    expect(localEventServiceSpy).toHaveBeenCalled();
  });

  it('should call filterData onFilterChange change', () => {
    const tableChildComponent = jasmine.createSpyObj('PageableDynamicTableComponent', ['filterData']);
    component.table = tableChildComponent;

    component.onFilterChange('abc');
    expect(tableChildComponent.filterData).toHaveBeenCalledWith('abc');
    expect(tableChildComponent.filterData).toHaveBeenCalledTimes(1);
  });
});
