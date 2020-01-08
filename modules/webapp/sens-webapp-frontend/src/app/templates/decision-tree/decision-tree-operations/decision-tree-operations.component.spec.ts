import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../../../shared/security/auth.service';
import { Authority } from '../../../shared/security/principal.model';
import { TestModule } from '../../../test/test.module';
import {
  DecisionTreeOperation,
  DecisionTreeOperationService
} from './decision-tree-operation.service';

import { DecisionTreeOperationsComponent } from './decision-tree-operations.component';
import { DecisionTreeOperationsModule } from './decision-tree-operations.module';

describe('DecisionTreeOperationsComponent', () => {
  let component: DecisionTreeOperationsComponent;
  let fixture: ComponentFixture<DecisionTreeOperationsComponent>;

  let authService: AuthService;
  let service: DecisionTreeOperationService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            DecisionTreeOperationsModule
          ]
        })
        .compileComponents();

    authService = TestBed.get(AuthService);
    service = TestBed.get(DecisionTreeOperationService);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeOperationsComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should not initialize any modal component when user has not authority', () => {
    spyOn(authService, 'hasAuthority')
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.createComponent).toBeFalsy();
    expect(component.editComponent).toBeFalsy();
    expect(component.copyComponent).toBeFalsy();
    expect(component.removeComponent).toBeFalsy();
    expect(component.assignComponent).toBeFalsy();
  });

  it('should do nothing and no errors should be thrown when user has no authority and call openOperationWindow', () => {
    spyOn(authService, 'hasAuthority')
        .and.returnValue(false);

    fixture.detectChanges();

    service.openOperationWindow(DecisionTreeOperation.CREATE);
    service.openOperationWindow(DecisionTreeOperation.EDIT);
    service.openOperationWindow(DecisionTreeOperation.COPY);
    service.openOperationWindow(DecisionTreeOperation.REMOVE);
    service.openOperationWindow(DecisionTreeOperation.ASSIGN);

    expect().nothing();
  });

  it('should init create, edit, copy, remove components, when user has DECISION_TREE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.createComponent).toBeTruthy();
    expect(component.editComponent).toBeTruthy();
    expect(component.copyComponent).toBeTruthy();
    expect(component.removeComponent).toBeTruthy();
    expect(component.assignComponent).toBeFalsy();
  });

  it('should init assign component, when user has BATCH_TYPE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.BATCH_TYPE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    fixture.detectChanges();

    expect(component.createComponent).toBeFalsy();
    expect(component.editComponent).toBeFalsy();
    expect(component.copyComponent).toBeFalsy();
    expect(component.removeComponent).toBeFalsy();
    expect(component.assignComponent).toBeTruthy();
  });

  describe('Operation success test', () => {

    beforeEach(() => {
      spyOn(authService, 'hasAuthority').and.returnValue(true);
      spyOn(service, 'operationSuccess');

      fixture.detectChanges();
    });

    [
      {supplier: () => component.createComponent.create, operation: DecisionTreeOperation.CREATE},
      {supplier: () => component.editComponent.edit, operation: DecisionTreeOperation.EDIT},
      {supplier: () => component.copyComponent.copy, operation: DecisionTreeOperation.COPY},
      {supplier: () => component.removeComponent.remove, operation: DecisionTreeOperation.REMOVE},
      {supplier: () => component.assignComponent.edit, operation: DecisionTreeOperation.ASSIGN}
    ].forEach(c => {

      it(`should call operationSuccess ${c.operation} after emit event`, () => {
        c.supplier().emit();

        expect(service.operationSuccess).toHaveBeenCalledWith(c.operation);
      });
    });
  });

  describe('Show test', () => {

    beforeEach(() => {
      spyOn(authService, 'hasAuthority').and.returnValue(true);

      fixture.detectChanges();
    });

    [
      {supplier: () => component.createComponent, operation: DecisionTreeOperation.CREATE},
      {supplier: () => component.editComponent, operation: DecisionTreeOperation.EDIT},
      {supplier: () => component.copyComponent, operation: DecisionTreeOperation.COPY},
      {supplier: () => component.removeComponent, operation: DecisionTreeOperation.REMOVE},
      {supplier: () => component.assignComponent, operation: DecisionTreeOperation.ASSIGN}
    ].forEach(c => {
      it(`should show ${c.operation} modal when openOperationWindow with ${c.operation} operation`, async(() => {
        service.openOperationWindow(c.operation, {id: 1, name: 'name'});

        expect(c.supplier().show).toBeTruthy();
      }));
    });
  });
});
