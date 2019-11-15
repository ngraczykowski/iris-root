import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BatchTypeListComponent } from './batch-type-list.component';
import { TestModule } from '@app/test/test.module';
import { BatchTypeManagementModule } from '../batch-type-management.module';
import { BatchTypeManagementService } from '../batch-type-management.service';
import { of } from 'rxjs/internal/observable/of';

describe('BatchTypeListComponent', () => {
  let component: BatchTypeListComponent;
  let fixture: ComponentFixture<BatchTypeListComponent>;
  let batchManagementService: BatchTypeManagementService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule, BatchTypeManagementModule ],
      providers: [BatchTypeManagementService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BatchTypeListComponent);
    batchManagementService = TestBed.get(BatchTypeManagementService);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('checkBatchVisibility', () => {
    it('should return true if batch canActivate is true', () => {
      expect(component.checkBatchVisibility({canActivate: true, name: 'test'})).toBeTruthy();
    });

    it('should return showActiveBatchTypes when batch canActivate is false', () => {
      component.showActiveBatches = false;
      expect(component.checkBatchVisibility({canActivate: false, name: 'test'})).toBeFalsy();
      component.showActiveBatches = true;
      expect(component.checkBatchVisibility({canActivate: false, name: 'test'})).toBeTruthy();
    });
  });

  describe('uncheckAll', () => {
    it('should call removeCheckedElement on BatchTypeManagementService', () => {
      component.batchTypes = of([
        { name: 'test', canActivate: true },
        { name: 'test2', canActivate: false },
      ]);

      const spy = spyOn(batchManagementService, 'removeCheckedElement');

      component.uncheckAll();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledTimes(2);
    });
  });

  describe('checkAll', () => {
    let spy;
    beforeEach(() => {
      component.batchTypes = of([
        { name: 'test', canActivate: true },
        { name: 'test2', canActivate: false },
      ]);
      spy = spyOn(batchManagementService, 'setCheckedElements');
    });


    it('should call setCheckedElements on BatchTypeManagementService with no filter attached', () => {
      component.batchTypeNameFilter = '';
      component.checkAll();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledTimes(2);
    });

    it('should call once setCheckedElements on BatchTypeManagementService with filter attached', () => {
      component.batchTypeNameFilter = 'test2';
      component.checkAll();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledTimes(1);
    });
  });
});
