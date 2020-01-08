import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AuthService } from '../../../../../../shared/security/auth.service';
import { Authority } from '../../../../../../shared/security/principal.model';
import { TestModule } from '../../../../../../test/test.module';
import { DecisionTreeTableViewsModule } from '../decision-tree-table-views.module';

import {
  DecisionTreeNameViewComponent,
  DecisionTreeNameViewData
} from './decision-tree-name-view.component';

describe('DecisionTreeNameViewComponent', () => {
  let component: DecisionTreeNameViewComponent;
  let fixture: ComponentFixture<DecisionTreeNameViewComponent>;

  let authService: AuthService;

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
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecisionTreeNameViewComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('should showLink be true when user has DECISION_TREE_LIST authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_LIST).and.returnValue(true);

    fixture.detectChanges();

    expect(component.showLink).toBeTruthy();
  });

  it('should showLink be false when user does not have DECISION_TREE_LIST authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_LIST).and.returnValue(false);

    fixture.detectChanges();

    expect(component.showLink).toBeFalsy();
  });

  it('should set valid decision tree link', () => {
    fixture.detectChanges();
    component.data = <DecisionTreeNameViewData> {decisionTreeId: 1, decisionTreeName: 'name'};

    expect(component.link).toEqual('/decision-tree/1');
  });

  it('should set valid decision tree name', () => {
    fixture.detectChanges();
    component.data = <DecisionTreeNameViewData> {decisionTreeId: 1, decisionTreeName: 'name'};

    expect(component.decisionTreeName).toEqual('name');
  });
});
