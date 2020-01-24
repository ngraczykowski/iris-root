import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DecisionGroup } from '@app/templates/decision-tree/decision-tree-list/decision-tree-table/views/decision-tree-activations-view/decision-group-list-builder';
import { TestModule } from '@app/test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeActivationsViewComponent } from './decision-tree-activations-view.component';

const fakeTooltipLongDecisionGroupsList: DecisionGroup[] = [
  { value: 'BT1' },
  { value: 'BT2' },
  { value: 'BT3' },
  { value: 'BT4' },
  { value: 'BT5' },
  { value: 'BT6' },
  { value: 'BT7' },
  { value: 'BT8' },
  { value: 'BT9' },
  { value: 'BT10' }
];

const fakeTooltipShortDecisionGroupsList: DecisionGroup[] = [
  { value: 'BT1' },
  { value: 'BT2' },
  { value: 'BT3' },
  { value: 'BT4' },
  { value: 'BT5' },
  { value: 'BT6' }
];

const fakeVeryShortDecisionGroupsList: DecisionGroup[] = [
  { value: 'BT1' },
  { value: 'BT2' }
];

const fakeEmptyDecisionGroupsList: DecisionGroup[] = [];

describe('DecisionTreeActivationsViewComponent', () => {
  let component: DecisionTreeActivationsViewComponent;
  let fixture: ComponentFixture<DecisionTreeActivationsViewComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            DecisionTreeTableViewsModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeActivationsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should show empty state when list of BT is empty', () => {
    component.decisionGroups = fakeEmptyDecisionGroupsList;

    fixture.detectChanges();

    const btEmptyState = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(btEmptyState).toBeTruthy();
  });

  it('Should hide empty state when list of BT is not empty', () => {
    component.decisionGroups = fakeTooltipLongDecisionGroupsList;

    fixture.detectChanges();

    const btEmptyState = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(btEmptyState).toBeFalsy();
  });

  it('Should show Tooltip when list of BT is longer than var "showTooltipForListLongerThan"', () => {
    component.decisionGroups = fakeTooltipShortDecisionGroupsList;

    fixture.detectChanges();

    const btTooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(btTooltip).toBeTruthy();
  });

  it('Should hide Tooltip when list of BT is shorter than var "showTooltipForListLongerThan"', () => {
    component.decisionGroups = fakeVeryShortDecisionGroupsList;

    fixture.detectChanges();

    const btTooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(btTooltip).toBeFalsy();
  });

  it('Should add class "tooltip-bt-list-no-limited" to list tag when list of BT is shorter than var "decisionGroupsTooltipListLimit"',
      () => {
    component.decisionGroups = fakeTooltipShortDecisionGroupsList;

    fixture.detectChanges();

    const btList = fixture.debugElement.query(By.css('#bt-tooltip-list'));
    expect(btList.classes['tooltip-bt-list-no-limited']).toBeTruthy();
  });

  it('Should show element with class "bt-list-limit" when list of BT is longer than var "decisionGroupsTooltipListLimit"', () => {
    component.decisionGroups = fakeTooltipLongDecisionGroupsList;

    fixture.detectChanges();

    const btTooltipLimitMessage = fixture.debugElement.query(By.css('.bt-list-limit'));
    expect(btTooltipLimitMessage).toBeTruthy();
  });

  it('Should hide element with class "bt-list-limit" when list of BT is shorter than var "decisionGroupsTooltipListLimit"', () => {
    component.decisionGroups = fakeTooltipShortDecisionGroupsList;

    fixture.detectChanges();

    const btTooltipLimitMessage = fixture.debugElement.query(By.css('.bt-list-limit'));
    expect(btTooltipLimitMessage).toBeFalsy();
  });
});
