import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';
import { DecisionTreeDetails } from '../../../model/decision-tree.model';
import { DecisionTreeDetailsModule } from '../decision-tree-details.module';

import { DecisionTreeInfoComponent } from './decision-tree-info.component';

describe('DecisionTreeInfoComponent', () => {
  let component: DecisionTreeInfoComponent;
  let fixture: ComponentFixture<DecisionTreeInfoComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeDetailsModule]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load assignments and activations correctly', () => {
    component.decisionTreeDetails = <DecisionTreeDetails> {
      agents: [],
      model: {},
      outputPorts: [],
      permissions: [],
      summary: {},
      status: {},
      assignments: ['1', '2', '3', '4'],
      activations: ['3', '4', '5', '6']
    };

    expect(component.assignments).toEqual(['1', '2']);
    expect(component.activations).toEqual(['3', '4', '5', '6']);
  });
});
