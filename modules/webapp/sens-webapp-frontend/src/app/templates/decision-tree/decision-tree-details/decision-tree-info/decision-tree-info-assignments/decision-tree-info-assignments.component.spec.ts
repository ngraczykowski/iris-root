import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { DecisionTreeInfoAssignmentsComponent } from './decision-tree-info-assignments.component';
import { DecisionTreeBatchTypesListComponent } from './decision-tree-batch-types-list/decision-tree-batch-types-list.component';

describe('DecisionTreeInfoAssignmentsComponent', () => {
  let component: DecisionTreeInfoAssignmentsComponent;
  let fixture: ComponentFixture<DecisionTreeInfoAssignmentsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ DecisionTreeInfoAssignmentsComponent, DecisionTreeBatchTypesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoAssignmentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
