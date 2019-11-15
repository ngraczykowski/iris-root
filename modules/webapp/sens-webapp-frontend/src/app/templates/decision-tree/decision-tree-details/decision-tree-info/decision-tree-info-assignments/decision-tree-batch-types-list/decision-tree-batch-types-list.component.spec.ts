import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { DecisionTreeBatchTypesListComponent } from './decision-tree-batch-types-list.component';

describe('DecisionTreeBatchTypesListComponent', () => {
  let component: DecisionTreeBatchTypesListComponent;
  let fixture: ComponentFixture<DecisionTreeBatchTypesListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ DecisionTreeBatchTypesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeBatchTypesListComponent);
    component = fixture.componentInstance;
    component.batchTypeList = ['a', 'c', 'b'];
    component.batchTypeStatus = 'Test';
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
