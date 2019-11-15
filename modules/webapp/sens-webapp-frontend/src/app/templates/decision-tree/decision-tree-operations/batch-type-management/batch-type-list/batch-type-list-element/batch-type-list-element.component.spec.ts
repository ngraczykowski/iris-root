import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BatchTypeListElementComponent } from './batch-type-list-element.component';
import { TestModule } from '@app/test/test.module';
import { BatchTypeManagementService } from '../../batch-type-management.service';

describe('BatchTypeListElementComponent', () => {
  let component: BatchTypeListElementComponent;
  let fixture: ComponentFixture<BatchTypeListElementComponent>;
  let batchManagementService: BatchTypeManagementService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ BatchTypeListElementComponent ],
      providers: [ BatchTypeManagementService ]
    })
    .compileComponents();
    batchManagementService = TestBed.get(BatchTypeManagementService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BatchTypeListElementComponent);
    component = fixture.componentInstance;
    component.batchType = {
      canActivate: true
    };

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('canActivate', () => {
    beforeEach(() => {
      component.batchType = {
        canActivate: true,
        activeForId: 1,
        activeForName: 'test'
      };
    });

    it('should return true when batchType is active', () => {
      expect(component.canActivate()).toBeTruthy();
    });
  });

  describe('checkElement', () => {
    it('should call setCheckedElements on batchManagementService when element is checked', () => {
      const element = document.createElement('input');
      element.checked = true;

      const spy = spyOn(batchManagementService, 'setCheckedElements');
      component.checkElement(element);
      fixture.detectChanges();
      expect(spy).toHaveBeenCalled();
      expect(spy).toHaveBeenCalledTimes(1);
      expect(spy).toHaveBeenCalledWith(component.batchType);
    });
  });
});
