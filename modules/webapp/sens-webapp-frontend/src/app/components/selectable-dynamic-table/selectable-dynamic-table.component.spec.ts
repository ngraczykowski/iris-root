import { Type } from '@angular/core';
import { async, ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { Observable, of } from 'rxjs';
import { TestModule } from '../../test/test.module';
import { Row, TableData } from '../dynamic-view-table/dynamic-view-table.component';
import { PageSelectorComponent } from '../pageable-dynamic-table/page-selector/page-selector.component';
import {
  TablePage,
  TablePageLoader,
  TablePageMapper
} from '../pageable-dynamic-table/simple-table-data-provider';
import {
  SelectableDynamicTableComponent,
  SelectableItemDiscriminator,
} from './selectable-dynamic-table.component';
import { SelectableDynamicTableModule } from './selectable-dynamic-table.module';
import { SelectableItemStore } from './selectable-item-store';
import { SelectAllViewComponent } from './views/select-all-view/select-all-view.component';
import { SelectOneViewComponent } from './views/select-one-view/select-one-view.component';

export class TestItem {
  id: string;
}

export class TestItemDiscriminator implements SelectableItemDiscriminator<TestItem> {
  getId(item: TestItem): any {
    return item.id;
  }
}

export class TestItemPageLoader implements TablePageLoader<TestItem> {
  load(page: number, size: number): Observable<TablePage<TestItem>> {
    const items = [];
    for (let i = 0; i < size; i++) {
      items.push(<TestItem> {id: `id${((page - 1) * size) + i}`});
    }
    return of(<TablePage<TestItem>> {
      total: 20,
      items: items
    });
  }
}

export class TestItemPageMapper implements TablePageMapper<TestItem> {
  map(page: TablePage<TestItem>): TableData {
    return <TableData> {
      total: page.total,
      labels: [],
      rows: page.items.map(i => <Row> {views: []})
    };
  }
}

describe('SelectableDynamicTableComponent', () => {
  let component: SelectableDynamicTableComponent<any>;
  let fixture: ComponentFixture<SelectableDynamicTableComponent<any>>;

  let discriminator: SelectableItemDiscriminator<TestItem>;
  let store: SelectableItemStore<TestItem>;
  let loader: TablePageLoader<TestItem>;
  let mapper: TablePageMapper<TestItem>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            SelectableDynamicTableModule
          ]
        })
        .compileComponents();

    discriminator = new TestItemDiscriminator();
    store = new SelectableItemStore<TestItem>();
    loader = new TestItemPageLoader();
    mapper = new TestItemPageMapper();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SelectableDynamicTableComponent);
    component = fixture.componentInstance;
    component.providerConfig = {
      discriminator: discriminator,
      store: store,
      loader: loader,
      mapper: mapper
    };
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create 1 select all and 10 select one elements when rowCountPerPage = 10', fakeAsync(() => {
    changeRowCountPerPage(10);

    expect(getSelectAllCount()).toEqual(1);
    expect(getSelectOneCount()).toEqual(10);
  }));

  it('should store all items when toggle select all', fakeAsync(() => {
    changeRowCountPerPage(3);

    toggleSelectAll();

    expect(store.getAll()).toEqual([
      <TestItem> {id: 'id0'},
      <TestItem> {id: 'id1'},
      <TestItem> {id: 'id2'}
    ]);
  }));

  it('should not have stored any item when toggle select all twice', fakeAsync(() => {
    changeRowCountPerPage(3);

    toggleSelectAll();
    toggleSelectAll();

    expect(store.getAll()).toEqual([]);
  }));

  it('should store valid items when toggle some select one views', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectOne(0);
    toggleSelectOne(2);
    toggleSelectOne(4);

    expect(store.getAll()).toEqual([
      <TestItem> {id: 'id0'},
      <TestItem> {id: 'id2'},
      <TestItem> {id: 'id4'}
    ]);
  }));

  it('should not have stored item when toggle select one view twice', fakeAsync(() => {
    changeRowCountPerPage(3);

    toggleSelectOne(0);
    toggleSelectOne(0);

    expect(store.getAll()).toEqual([]);
  }));

  it('should refresh select one after change rowCountPerPage', fakeAsync(() => {
    changeRowCountPerPage(3);
    expect(getSelectOneCount()).toEqual(3);

    changeRowCountPerPage(10);
    expect(getSelectOneCount()).toEqual(10);
  }));

  it('should keep stored items after change rowCountPerPage', fakeAsync(() => {
    changeRowCountPerPage(10);

    toggleSelectOne(0);
    toggleSelectOne(5);
    toggleSelectOne(9);

    changeRowCountPerPage(3);

    expect(store.getAll()).toEqual([
      <TestItem> {id: 'id0'},
      <TestItem> {id: 'id5'},
      <TestItem> {id: 'id9'}
    ]);
  }));

  it('should keep stored items after change page', fakeAsync(() => {
    changeRowCountPerPage(10);

    toggleSelectOne(0);
    changePage(2);

    expect(store.getAll()).toEqual([
      <TestItem> {id: 'id0'}
    ]);
  }));

  it('should store valid items from different pages', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectOne(0);
    toggleSelectOne(2);

    changePage(2);
    toggleSelectOne(4);

    changePage(3);
    toggleSelectAll();

    changePage(1);
    toggleSelectOne(2);

    expect(store.getAll()).toEqual([
      <TestItem> {id: 'id0'},
      <TestItem> {id: 'id9'},
      <TestItem> {id: 'id10'},
      <TestItem> {id: 'id11'},
      <TestItem> {id: 'id12'},
      <TestItem> {id: 'id13'},
      <TestItem> {id: 'id14'}
    ]);
  }));

  it('should select one be checked after toggle', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectOne(2);

    expect(isSelectOneChecked(2)).toBeTruthy();
  }));

  it('should select one be unchecked after toggle twice', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectOne(2);
    toggleSelectOne(2);

    expect(isSelectOneChecked(2)).toBeFalsy();
  }));

  it('should select all be checked after toggle', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectAll();

    expect(isSelectAllChecked()).toBeTruthy();
  }));

  it('should select all be unchecked after toggle twice', fakeAsync(() => {
    changeRowCountPerPage(5);

    toggleSelectAll();
    toggleSelectAll();

    expect(isSelectAllChecked()).toBeFalsy();
  }));

  it('should select all be checked after toggle all one by one', fakeAsync(() => {
    changeRowCountPerPage(3);

    toggleSelectOne(0);
    toggleSelectOne(1);
    toggleSelectOne(2);

    expect(isSelectAllChecked()).toBeTruthy();
  }));

  it('should select all be unchecked after unselect one', fakeAsync(() => {
    changeRowCountPerPage(3);

    toggleSelectAll();
    toggleSelectOne(0);

    expect(isSelectAllChecked()).toBeFalsy();
  }));

  function changeRowCountPerPage(value) {
    component.rowCountPerPage = value;
    fixture.detectChanges();
    tick();
  }

  function isSelectAllChecked(): boolean {
    return findComponent(SelectAllViewComponent).isChecked();
  }

  function isSelectOneChecked(index): boolean {
    return findComponents(SelectOneViewComponent)[index].isChecked();
  }

  function toggleSelectAll() {
    findComponent(SelectAllViewComponent).toggle();
  }

  function toggleSelectOne(index) {
    findComponents(SelectOneViewComponent)[index].toggle();
  }

  function changePage(page) {
    findComponent(PageSelectorComponent).onSelectPage(page);
    fixture.detectChanges();
    tick();
  }

  function getSelectAllCount() {
    return findComponents(SelectAllViewComponent).length;
  }

  function getSelectOneCount() {
    return findComponents(SelectOneViewComponent).length;
  }

  function findComponent<T>(type: Type<T>): T {
    fixture.detectChanges();
    return fixture.debugElement.query(By.directive(type)).componentInstance;
  }

  function findComponents<T>(type: Type<T>): T[] {
    fixture.detectChanges();
    return fixture.debugElement.queryAll(By.directive(type)).map(d => d.componentInstance);
  }
});
