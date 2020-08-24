import { TestBed } from '@angular/core/testing';

import { UserManagementListService } from './user-management-list.service';

describe('UserManagementListService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserManagementListService = TestBed.get(UserManagementListService);
    expect(service).toBeTruthy();
  });
});
