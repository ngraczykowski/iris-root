import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { TestModule } from '../../../../../test/test.module';
import { DecisionTree } from '../../../../model/decision-tree.model';
import { UserDecisionTreeSelectComponent } from './user-decision-tree-select.component';

describe('UserDecisionTreeSelectComponent', () => {
  let component: UserDecisionTreeSelectComponent;
  let fixture: ComponentFixture<UserDecisionTreeSelectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule],
      declarations: [UserDecisionTreeSelectComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserDecisionTreeSelectComponent);
    component = fixture.componentInstance;
    component.control = new FormArray([new FormGroup({
      decisionTreeId: new FormControl(1)
    })]);
    component.decisionTrees = [createDt(1, 'DT 1')];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  function createDt(id, name) {
    return <DecisionTree> {
      id: id,
      name: name
    };
  }
});
