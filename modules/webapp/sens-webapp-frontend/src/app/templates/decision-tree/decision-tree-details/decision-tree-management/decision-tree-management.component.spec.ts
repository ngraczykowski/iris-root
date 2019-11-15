import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '@app/shared/auth/auth.service';
import { Authority, Principal } from '@app/shared/auth/principal.model';
import { DecisionTreeDetailsModule } from '@app/templates/decision-tree/decision-tree-details/decision-tree-details.module';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from '@app/templates/decision-tree/decision-tree-operations/decision-tree-operation.service';
import { DecisionTreeDetails } from '@app/templates/model/decision-tree.model';
import { TestModule } from '@app/test/test.module';

import { DecisionTreeManagementComponent } from './decision-tree-management.component';

describe('DecisionTreeManagementComponent', () => {
  let component: DecisionTreeManagementComponent;
  let fixture: ComponentFixture<DecisionTreeManagementComponent>;

  let operationService: DecisionTreeOperationService;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeDetailsModule]
        })
        .compileComponents();

    operationService = TestBed.get(DecisionTreeOperationService);
    authService = TestBed.get(AuthService);
    authService.principal = new Principal('test', 'test', ['test'], true);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeManagementComponent);
    component = fixture.componentInstance;
    component.decisionTree = <DecisionTreeDetails>{ id: 1, name: 'name', permissions: [] };
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should hasPermissionToRenameTree be false when user does not have DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(false)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToRenameTree).toBeFalsy();
  });

  it('should hasPermissionToRenameTree be true when user has DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToRenameTree).toBeTruthy();
  });

  it('should hasPermissionToCopyTree be false when user does not have DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(false)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToCopyTree).toBeFalsy();
  });

  it('should hasPermissionToCopyTree be true when user has DECISION_TREE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToCopyTree).toBeTruthy();
  });

  it('should hasPermissionToRemoveTree be false when user does not have Superuser Permissions', () => {
    spyOn(authService, 'hasSuperuserPermissions')
        .withArgs().and.returnValue(false)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToCopyTree).toBeFalsy();
  });

  it('should hasPermissionToRemoveTree be true when user has Superuser Permissions', () => {
    spyOn(authService, 'hasSuperuserPermissions')
        .withArgs().and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasPermissionToRemoveTree).toBeTruthy();
  });

  it('should hasBatchTypeManageAccess be false when user does not have BATCH_TYPE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.BATCH_TYPE_MANAGE).and.returnValue(false)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasBatchTypeManageAccess).toBeFalsy();
  });

  it('should hasBatchTypeManageAccess be true when user has BATCH_TYPE_MANAGE', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.BATCH_TYPE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.hasBatchTypeManageAccess).toBeTruthy();
  });

  [
    {supplier: () => component.onRenameDecisionTree(), operation: DecisionTreeOperation.EDIT},
    {supplier: () => component.onAssignDecisionTree(), operation: DecisionTreeOperation.ASSIGN},
    {supplier: () => component.onCopyDecisionTree(), operation: DecisionTreeOperation.COPY},
    {supplier: () => component.onRemoveDecisionTree(), operation: DecisionTreeOperation.REMOVE}
  ].forEach(c => {
    it(`should openOperationWindow with ${c.operation} operation as parameter`, () => {
      spyOn(operationService, 'openOperationWindow');
      fixture.detectChanges();

      c.supplier();

      expect(operationService.openOperationWindow)
          .toHaveBeenCalledWith(c.operation, component.decisionTree);
    });
  });
});
