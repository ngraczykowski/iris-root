import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../../../../../shared/auth/auth.service';
import { Authority } from '../../../../../shared/auth/principal.model';
import { TestModule } from '../../../../../test/test.module';
import { WorkflowViewModule } from '../workflow-view.module';
import {
  WorkflowDecisionTreeNameViewComponent,
  WorkflowDecisionTreeNameViewData
} from './workflow-decision-tree-name-view.component';

describe('WorkflowDecisionTreeNameViewComponent', () => {
  let component: WorkflowDecisionTreeNameViewComponent;
  let fixture: ComponentFixture<WorkflowDecisionTreeNameViewComponent>;

  let authorities: Authority[];

  let authService: AuthService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            WorkflowViewModule
          ]
        })
        .compileComponents();

    authService = TestBed.get(AuthService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowDecisionTreeNameViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    authorities = [];
    spyOn(authService, 'hasAuthority').and.callFake(p => authorities.indexOf(p) !== -1);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should link be valid after set data', () => {
    component.data = <WorkflowDecisionTreeNameViewData> {
      decisionTreeName: 'name',
      decisionTreeId: 1
    };

    expect(component.link).toEqual('/decision-tree/1');
  });

  it('should decisionTreeName be valid after set data', () => {
    component.data = <WorkflowDecisionTreeNameViewData> {
      decisionTreeName: 'name',
      decisionTreeId: 1
    };

    expect(component.decisionTreeName).toEqual('name');
  });

  describe('given user with DECISION_TREE_LIST authority', () => {
    beforeEach(() => {
      authorities.push(Authority.DECISION_TREE_LIST);
    });

    it('should showLink be truthy after set data', () => {
      component.data = <WorkflowDecisionTreeNameViewData> {
        decisionTreeName: 'name',
        decisionTreeId: 1
      };

      expect(component.showLink).toBeTruthy();
    });
  });

  describe('given user without DECISION_TREE_LIST authority', () => {

    it('should showLink be falsy after set data', () => {
      component.data = <WorkflowDecisionTreeNameViewData> {
        decisionTreeName: 'name',
        decisionTreeId: 1
      };

      expect(component.showLink).toBeFalsy();
    });
  });
});
