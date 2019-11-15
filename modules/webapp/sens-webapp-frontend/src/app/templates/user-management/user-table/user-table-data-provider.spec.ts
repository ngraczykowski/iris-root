import { async, TestBed } from '@angular/core/testing';
import { TableData } from '../../../components/dynamic-view-table/dynamic-view-table.component';
import { CollectionResponse } from '../../model/collection-response.model';
import { User } from '../../model/user.model';
import { UserTableDataMapper, UserTableDataProvider } from './user-table-data-provider';
import { Store, MemoizedSelectorWithProps } from '@ngrx/store';
import { MockStore, provideMockStore } from '@ngrx/store/testing';
import * as fromRoot from '@app/reducers/index';

describe('UserTableDataProvider', () => {
  let mapper: UserTableDataMapper;
  let provider: UserTableDataProvider;
  let store: MockStore<fromRoot.State>;
  let usersTable: MemoizedSelectorWithProps<fromRoot.State, {page: number, size: number}, any>;
  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          providers: [
            UserTableDataMapper,
            UserTableDataProvider,
            provideMockStore()
          ]
        });
    store = TestBed.get(Store);
    mapper = TestBed.get(UserTableDataMapper);
    provider = TestBed.get(UserTableDataProvider);
    usersTable = store.overrideSelector(fromRoot.getUsersTablePage, {total: 10, results: []});
  }));

  it('should return mapped client response object', done => {
    const response: CollectionResponse<User> = {total: 10, results: []};
    const mappedResponse: TableData = {total: 10, labels: [], rows: []};
    spyOn(mapper, 'map').and.returnValue(mappedResponse);

    provider.getPage(2, 10).subscribe((data) => {
      expect(mapper.map).toHaveBeenCalledWith(response.total, response.results);
      expect(data).toEqual(mappedResponse);
      done();
    });
  });
});
