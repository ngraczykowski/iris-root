import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../test/test.module';
import { DecisionTreeOperationsModule } from '../decision-tree-operations.module';
import { CreateNewDecisionTreeComponent } from './create-new-decision-tree.component';

describe('CreateNewDecisionTreeComponent', () => {
  let component: CreateNewDecisionTreeComponent;
  let fixture: ComponentFixture<CreateNewDecisionTreeComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeOperationsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CreateNewDecisionTreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
