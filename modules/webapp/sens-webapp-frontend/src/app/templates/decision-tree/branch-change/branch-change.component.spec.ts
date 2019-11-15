import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { BranchUpdateStateData } from '@app/templates/decision-tree/branch-change/branch-change.component';
import { TestModule } from '@app/test/test.module';
import { BranchChangeStateService, } from './branch-change-state.service';

import {
  BranchChangeComponent,
  BranchChangeState,
  BranchChangeStateType,
  BranchSelectStateData
} from './branch-change.component';
import { BranchChangeModule } from './branch-change.module';

describe('BranchChangeComponent', () => {
  let component: BranchChangeComponent;
  let fixture: ComponentFixture<BranchChangeComponent>;

  let service: BranchChangeStateService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchChangeModule
          ]
        })
        .compileComponents();

    service = TestBed.get(BranchChangeStateService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchChangeComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should init with select state', () => {
    component.decisionTreeId = 1;

    fixture.detectChanges();

    expect(component.state).toEqual(<BranchChangeState> {
      type: BranchChangeStateType.SELECT,
      data: <BranchSelectStateData> {
        decisionTreeId: 1
      }
    });
  });

  it('should change component state when change state on service', () => {
    const data = <BranchUpdateStateData> {decisionTreeId: 2};
    fixture.detectChanges();

    service.setUpdateState(data);

    expect(component.state).toEqual(<BranchChangeState> {
      type: BranchChangeStateType.UPDATE,
      data: data
    });
  });
});
