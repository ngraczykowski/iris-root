import { TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { BatchTypeManagementService } from './batch-type-management.service';

describe('BatchTypeManagementService', () => {
  let batchTypeManagementService: BatchTypeManagementService;
  beforeEach(async() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      providers: [BatchTypeManagementService]
    }).compileComponents();

    batchTypeManagementService = TestBed.get(BatchTypeManagementService);
  });

  describe('unchecking elements', () => {
    beforeEach(() => {
      batchTypeManagementService.checkedElements.next([{name: 'test_1'}, {name: 'test_2'}]);
    });

    it('should uncheck not matching query batches', () => {
      batchTypeManagementService.uncheckNotMatchingFilterQuery('test_1');
      expect(batchTypeManagementService.checkedElements.value).toEqual([{name: 'test_1'}]);
    });

    it('should uncheck checked batch', () => {
      batchTypeManagementService.removeCheckedElement({name: 'test_1', canActivate: true});
      expect(batchTypeManagementService.checkedElements.value).toEqual([{name: 'test_2'}]);
    });

    it('should uncheck all elements on uncheckAll', () => {
      batchTypeManagementService.uncheckAll();
      expect(batchTypeManagementService.checkedElements.value.length).toBe(0);
    });
  });
});
