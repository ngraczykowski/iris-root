import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { EMPTY, Observable, of, throwError, timer } from 'rxjs';
import 'rxjs-compat/add/observable/of';
import 'rxjs-compat/add/observable/timer';
import 'rxjs-compat/add/operator/delay';
import { mapTo, tap } from 'rxjs/operators';
import { TestModule } from '../../test/test.module';
import { TableData } from '../dynamic-view-table/dynamic-view-table.component';
import {
  PageableDynamicTableComponent,
  TableDataProvider
} from './pageable-dynamic-table.component';
import { PageableDynamicTableModule } from './pageable-dynamic-table.module';
import { ActivatedRoute, ChildrenOutletContexts } from '@angular/router';
import { Subject } from 'rxjs';

const mockRoute: any  = { snapshot: {}};
mockRoute.parent = { params: new Subject<any>()};
mockRoute.params = new Subject<any>();
mockRoute.queryParams = new Subject<any>();

class TableDataProviderMock implements TableDataProvider {

  getPage(page: number, size: number): Observable<TableData> {
    return EMPTY;
  }
}

describe('PageableDynamicTableComponent', () => {
  let component: PageableDynamicTableComponent;
  let fixture: ComponentFixture<PageableDynamicTableComponent>;

  let provider: TableDataProvider;

  const paramsMock = { page: 2, rowSize: 10 };

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            PageableDynamicTableModule
          ],
          providers: [
            ChildrenOutletContexts,
            {
              provide: ActivatedRoute, useValue: mockRoute
            }
          ]
        })
        .compileComponents();

    provider = new TableDataProviderMock();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PageableDynamicTableComponent);
    component = fixture.componentInstance;
    component.provider = provider;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should get param from url', async() => {
    fixture.detectChanges();
    const spy = spyOn(component, 'handlePageQuery');

    mockRoute.queryParams.next(paramsMock);
    expect(spy).toHaveBeenCalledWith(paramsMock.page, paramsMock.rowSize);
  });

  it('should set page variable on handlePageQuery', () => {
    component.handlePageQuery(paramsMock.page, paramsMock.rowSize);
    fixture.detectChanges();

    expect(component.page).toBe(paramsMock.page);
  });

  it('should load page on change page', () => {
    component.rowCountPerPage = 20;
    fixture.detectChanges();

    const data = <TableData>{total: 10, labels: [], rows: []};
    spyOn(provider, 'getPage').and.returnValue(of(data));
    component.page = 2;

    expect(provider.getPage).toHaveBeenCalledWith(2, 20, undefined);
    expect(component.currentPageTableData).toEqual(data);
  });

  it('should set error flag when error occured while loading page', fakeAsync(() => {
    mockRoute.queryParams.next({ page: 1, rowSize: 10 });
    fixture.detectChanges();
    spyOn(provider, 'getPage').and.returnValue(throwError({}));

    component.rowCountPerPage = 10;
    fixture.detectChanges();
    tick();

    expect(component.error).toBeTruthy();
  }));

  it('should set inProgress flag while loading page', fakeAsync(() => {
    component.rowCountPerPage = 10;
    fixture.detectChanges();
    const data = <TableData>{total: 10, labels: [], rows: []};
    spyOn(provider, 'getPage').and.returnValue(timer(100).pipe(mapTo(data)));

    expect(component.inProgress).toBeFalsy();

    component.page = 2;

    expect(component.inProgress).toBeTruthy();
    tick(150);

    expect(component.inProgress).toBeFalsy();
  }));

  it('should cancel previous request when select page while previous has not been loaded yet', fakeAsync(() => {
    component.rowCountPerPage = 10;
    let previousRequestFinished = false;
    const getPageSpy = spyOn(provider, 'getPage');
    getPageSpy.and.returnValue(timer(100).pipe(tap(() => previousRequestFinished = true)));
    fixture.detectChanges();

    component.page = 1;
    tick(50);
    getPageSpy.and.returnValue(of(<TableData>{total: 10, labels: [], rows: []}));
    component.page = 2;
    tick(50);

    expect(previousRequestFinished).toBeFalsy();
  }));
});
