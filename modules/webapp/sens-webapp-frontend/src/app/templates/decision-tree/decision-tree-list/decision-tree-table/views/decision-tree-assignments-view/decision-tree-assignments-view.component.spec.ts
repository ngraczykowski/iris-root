import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { BatchType } from '@app/templates/decision-tree/decision-tree-list/decision-tree-table/views/decision-tree-assignments-view/batch-type-list-builder';
import { TestModule } from '@app/test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import { DecisionTreeAssignmentsViewComponent } from './decision-tree-assignments-view.component';

const fakeTooltipLongBatchTypesList: BatchType[] = [
  { value: 'BT1', active: true },
  { value: 'BT2', active: true },
  { value: 'BT3', active: true },
  { value: 'BT4', active: true },
  { value: 'BT5', active: true },
  { value: 'BT6', active: true },
  { value: 'BT7', active: true },
  { value: 'BT8', active: true },
  { value: 'BT9', active: true },
  { value: 'BT10', active: true }
];

const fakeTooltipShortBatchTypesList: BatchType[] = [
  { value: 'BT1', active: true },
  { value: 'BT2', active: true },
  { value: 'BT3', active: true },
  { value: 'BT4', active: true },
  { value: 'BT5', active: true },
  { value: 'BT6', active: true }
];

const fakeVeryShortBatchTypesList: BatchType[] = [
  { value: 'BT1', active: true },
  { value: 'BT2', active: true }
];

const fakeEmptyBatchTypesList: BatchType[] = [];

describe('DecisionTreeAssignmentsViewComponent', () => {
  let component: DecisionTreeAssignmentsViewComponent;
  let fixture: ComponentFixture<DecisionTreeAssignmentsViewComponent>;

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
    fixture = TestBed.createComponent(DecisionTreeAssignmentsViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should show empty state when list of BT is empty', () => {
    component.batchTypes = fakeEmptyBatchTypesList;

    fixture.detectChanges();

    const btEmptyState = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(btEmptyState).toBeTruthy();
  });

  it('Should hide empty state when list of BT is not empty', () => {
    component.batchTypes = fakeTooltipLongBatchTypesList;

    fixture.detectChanges();

    const btEmptyState = fixture.debugElement.query(By.css('.empty-state-data'));
    expect(btEmptyState).toBeFalsy();
  });

  it('Should show Tooltip when list of BT is longer than var "showTooltipForListLongerThan"', () => {
    component.batchTypes = fakeTooltipShortBatchTypesList;

    fixture.detectChanges();

    const btTooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(btTooltip).toBeTruthy();
  });

  it('Should hide Tooltip when list of BT is shorter than var "showTooltipForListLongerThan"', () => {
    component.batchTypes = fakeVeryShortBatchTypesList;

    fixture.detectChanges();

    const btTooltip = fixture.debugElement.query(By.css('.tooltip-layer'));
    expect(btTooltip).toBeFalsy();
  });

  it('Should add class "tooltip-bt-list-no-limited" to list tag when list of BT is shorter than var "batchTypesTooltipListLimit"', () => {
    component.batchTypes = fakeTooltipShortBatchTypesList;

    fixture.detectChanges();

    const btList = fixture.debugElement.query(By.css('#bt-tooltip-list'));
    expect(btList.classes['tooltip-bt-list-no-limited']).toBeTruthy();
  });

  it('Should show element with class "bt-list-limit" when list of BT is longer than var "batchTypesTooltipListLimit"', () => {
    component.batchTypes = fakeTooltipLongBatchTypesList;

    fixture.detectChanges();

    const btTooltipLimitMessage = fixture.debugElement.query(By.css('.bt-list-limit'));
    expect(btTooltipLimitMessage).toBeTruthy();
  });

  it('Should hide element with class "bt-list-limit" when list of BT is shorter than var "batchTypesTooltipListLimit"', () => {
    component.batchTypes = fakeTooltipShortBatchTypesList;

    fixture.detectChanges();

    const btTooltipLimitMessage = fixture.debugElement.query(By.css('.bt-list-limit'));
    expect(btTooltipLimitMessage).toBeFalsy();
  });
});
