import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '@app/shared/security/auth.service';
import { Authority, Principal } from '@app/shared/security/principal.model';
import { DecisionTreeTableViewsModule } from '@app/templates/decision-tree/decision-tree-list/decision-tree-table/views/decision-tree-table-views.module';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operation.service';
import { TestModule } from '@app/test/test.module';

import {
  DecisionTreeOperationsViewComponent,
  DecisionTreeOperationsViewData
} from './decision-tree-operations-view.component';

describe('DecisionTreeOperationsViewComponent', () => {
  let component: DecisionTreeOperationsViewComponent;
  let fixture: ComponentFixture<DecisionTreeOperationsViewComponent>;

  let authService: AuthService;
  let decisionTreeOperationService: DecisionTreeOperationService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            DecisionTreeTableViewsModule
          ]
        })
        .compileComponents();

    authService = TestBed.get(AuthService);
    authService.principal = new Principal('test', 'test', ['test'], true);
    decisionTreeOperationService = TestBed.get(DecisionTreeOperationService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeOperationsViewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should hasPermissionToCopyTree be false when user does not have DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToCopyTree).toBeFalsy();
  });

  it('should hasPermissionToCopyTree be true when user has DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true);

    fixture.detectChanges();

    expect(component.hasPermissionToCopyTree).toBeTruthy();
  });

  it('should hasPermissionToRemoveTree be false when user does not have Superuser Permissions', () => {
    spyOn(authService, 'hasSuperuserPermissions')
        .withArgs().and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToRemoveTree).toBeFalsy();
  });

  it('should hasPermissionToRemoveTree be true when user has Superuser Permissions', () => {
    spyOn(authService, 'hasSuperuserPermissions')
        .withArgs().and.returnValue(true);

    fixture.detectChanges();

    expect(component.hasPermissionToRemoveTree).toBeTruthy();
  });

  it('should invoke openOperationWindow with REMOVE operation onRemove', () => {
    spyOn(decisionTreeOperationService, 'openOperationWindow');
    component.data = <DecisionTreeOperationsViewData> {decisionTreeId: 1, decisionTreeName: 'name'};
    fixture.detectChanges();

    component.onRemove();

    expect(decisionTreeOperationService.openOperationWindow)
        .toHaveBeenCalledWith(DecisionTreeOperation.REMOVE, {id: 1, name: 'name'});
  });

  it('should invoke openOperationWindow with COPY operation onCopy', () => {
    spyOn(decisionTreeOperationService, 'openOperationWindow');
    component.data = <DecisionTreeOperationsViewData> {decisionTreeId: 1, decisionTreeName: 'name'};
    fixture.detectChanges();

    component.onCopy();

    expect(decisionTreeOperationService.openOperationWindow)
        .toHaveBeenCalledWith(DecisionTreeOperation.COPY, {id: 1, name: 'name'});
  });
});
