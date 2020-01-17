import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { DecisionTreeInfoActivationsComponent } from './decision-tree-info-activations.component';
import { DecisionTreeBatchTypesListComponent } from './decision-tree-batch-types-list/decision-tree-batch-types-list.component';

describe('DecisionTreeInfoActivationsComponent', () => {
  let component: DecisionTreeInfoActivationsComponent;
  let fixture: ComponentFixture<DecisionTreeInfoActivationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ DecisionTreeInfoActivationsComponent, DecisionTreeBatchTypesListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoActivationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
