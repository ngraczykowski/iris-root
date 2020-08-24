import { TestBed } from '@angular/core/testing';

import { UserManagementSearchService } from './user-management-search.service';

describe('UserManagementSearchService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserManagementSearchService = TestBed.get(UserManagementSearchService);
    expect(service).toBeTruthy();
  });
});
