import { TestBed } from '@angular/core/testing';

import { DecisionTreesService } from './decision-trees.service';

describe('DecisionTreesService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DecisionTreesService = TestBed.get(DecisionTreesService);
    expect(service).toBeTruthy();
  });
});
