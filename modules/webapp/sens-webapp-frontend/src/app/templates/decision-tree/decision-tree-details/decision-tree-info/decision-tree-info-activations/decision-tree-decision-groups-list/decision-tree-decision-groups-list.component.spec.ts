import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '@app/test/test.module';

import { DecisionTreeDecisionGroupsListComponent } from './decision-tree-decision-groups-list.component';

describe('DecisionTreeDecisionGroupsListComponent', () => {
  let component: DecisionTreeDecisionGroupsListComponent;
  let fixture: ComponentFixture<DecisionTreeDecisionGroupsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ DecisionTreeDecisionGroupsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeDecisionGroupsListComponent);
    component = fixture.componentInstance;
    component.decisionGroups = ['a', 'c', 'b'];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
