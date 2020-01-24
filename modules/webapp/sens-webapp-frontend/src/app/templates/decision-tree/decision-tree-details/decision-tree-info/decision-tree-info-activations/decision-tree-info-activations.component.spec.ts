import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../../test/test.module';

import { DecisionTreeInfoActivationsComponent } from './decision-tree-info-activations.component';
import { DecisionTreeDecisionGroupsListComponent } from './decision-tree-decision-groups-list/decision-tree-decision-groups-list.component';

describe('DecisionTreeInfoActivationsComponent', () => {
  let component: DecisionTreeInfoActivationsComponent;
  let fixture: ComponentFixture<DecisionTreeInfoActivationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ DecisionTreeInfoActivationsComponent, DecisionTreeDecisionGroupsListComponent ]
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
