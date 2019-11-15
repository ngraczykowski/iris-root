import {
  async,
  ComponentFixture,
  discardPeriodicTasks,
  fakeAsync,
  TestBed,
  tick
} from '@angular/core/testing';
import { AuthService } from '@app/shared/auth/auth.service';
import { Authority } from '@app/shared/auth/principal.model';
import { TestModule } from '@app/test/test.module';
import { CollectionResponse } from '@model/collection-response.model';
import { DecisionTree } from '@model/decision-tree.model';
import { EMPTY, of, timer } from 'rxjs';
import { mapTo } from 'rxjs/operators';
import { DecisionTreeListComponent } from './decision-tree-list.component';
import { DecisionTreeListModule } from './decision-tree-list.module';
import { DecisionTreeListService } from './decision-tree-list.service';

// TODO(iwnek)
describe('DecisionTreesComponent', () => {
  let component: DecisionTreeListComponent;
  let fixture: ComponentFixture<DecisionTreeListComponent>;

  let decisionTreesService: DecisionTreeListService;
  let authService: AuthService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, DecisionTreeListModule]
        })
        .compileComponents();

    decisionTreesService = TestBed.get(DecisionTreeListService);
    authService = TestBed.get(AuthService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(DecisionTreeListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(decisionTreesService, 'getActiveDecisionTrees').and.returnValue(EMPTY);
    spyOn(decisionTreesService, 'getInactiveDecisionTrees').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });

  it('should set firstTimeLoaded true, after first load', fakeAsync(() => {
    const response = <CollectionResponse<DecisionTree>>{total: 0, results: []};
    spyOn(decisionTreesService, 'getActiveDecisionTrees')
        .and.returnValue(timer(50).pipe(mapTo(response)));
    spyOn(decisionTreesService, 'getInactiveDecisionTrees')
        .and.returnValue(timer(50).pipe(mapTo(response)));
    initComponent();

    expect(component.firstTimeLoaded).toBeFalsy();
    tick(200);
    expect(component.firstTimeLoaded).toBeTruthy();

    discardPeriodicTasks();
  }));

  it('should hasDecisionTreeManageAccess be true if user has DECISION_TREE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(true)
        .and.returnValue(false);

    initComponent();

    expect(component.hasDecisionTreeManageAccess).toBeTruthy();
  });

  it('should hasDecisionTreeManageAccess be false if user does not have DECISION_TREE_MANAGE authority', () => {
    spyOn(authService, 'hasAuthority')
        .withArgs(Authority.DECISION_TREE_MANAGE).and.returnValue(false)
        .and.returnValue(false);

    initComponent();

    expect(component.hasDecisionTreeManageAccess).toBeFalsy();
  });

  it('should update decisionTrees only if server response changed', fakeAsync(() => {
    const dt1 = {
      id: 1,
      active: true,
      name: 'dt1',
      status: { name: 'PROCESSING' },
      assignments: ['BT1']
    } as DecisionTree;

    const dt2 = {
      id: 2,
      active: false,
      name: 'dt2',
      status: { name: 'PROCESSED' },
      assignments: ['BT2']
    } as DecisionTree;

    const response1 = <CollectionResponse<DecisionTree>>{ total: 0, results: [] };
    const response2 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };
    const response3 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };
    const response4 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt2] };
    const response5 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1, dt2] };
    const response6 = <CollectionResponse<DecisionTree>>{ total: 0, results: [dt1] };

    spyOn(decisionTreesService, 'getActiveDecisionTrees')
      .and.returnValues(
        of(response1),
        of(response2),
        of(response2),
        of(response3),
        of(response4),
        of(response5),
        of(response6)
      );
    spyOn(decisionTreesService, 'getInactiveDecisionTrees')
      .and.returnValues(
        of(response1),
        of(response2),
        of(response2),
        of(response3),
        of(response4),
        of(response5),
        of(response6)
      );

    initComponent();
    tick();

    let previousActive = component.activeDecisionTrees;
    let previousInactive = component.inactiveDecisionTrees;
    expect(component.activeDecisionTrees).toEqual(response1.results);
    expect(component.inactiveDecisionTrees).toEqual(response1.results);

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response2.results);
    expect(component.inactiveDecisionTrees).toEqual(response2.results);
    expect(component.activeDecisionTrees).not.toEqual(previousActive);
    expect(component.inactiveDecisionTrees).not.toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeFalsy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeFalsy();
    previousActive = component.activeDecisionTrees;
    previousInactive = component.inactiveDecisionTrees;

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response2.results);
    expect(component.inactiveDecisionTrees).toEqual(response2.results);
    expect(component.activeDecisionTrees).toEqual(previousActive);
    expect(component.inactiveDecisionTrees).toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeTruthy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeTruthy();
    previousActive = component.activeDecisionTrees;
    previousInactive = component.inactiveDecisionTrees;

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response3.results);
    expect(component.inactiveDecisionTrees).toEqual(response3.results);
    expect(component.activeDecisionTrees).toEqual(previousActive);
    expect(component.inactiveDecisionTrees).toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeTruthy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeTruthy();
    previousActive = component.activeDecisionTrees;
    previousInactive = component.inactiveDecisionTrees;

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response4.results);
    expect(component.inactiveDecisionTrees).toEqual(response4.results);
    expect(component.activeDecisionTrees).not.toEqual(previousActive);
    expect(component.inactiveDecisionTrees).not.toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeFalsy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeFalsy();
    previousActive = component.activeDecisionTrees;
    previousInactive = component.inactiveDecisionTrees;

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response5.results);
    expect(component.inactiveDecisionTrees).toEqual(response5.results);
    expect(component.activeDecisionTrees).not.toEqual(previousActive);
    expect(component.inactiveDecisionTrees).not.toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeFalsy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeFalsy();

    component.updateDecisionTrees();
    expect(component.activeDecisionTrees).toEqual(response6.results);
    expect(component.inactiveDecisionTrees).toEqual(response6.results);
    expect(component.activeDecisionTrees).not.toEqual(previousActive);
    expect(component.inactiveDecisionTrees).not.toEqual(previousInactive);
    expect(component.activeDecisionTrees === previousActive).toBeFalsy();
    expect(component.inactiveDecisionTrees === previousInactive).toBeFalsy();

    discardPeriodicTasks();
  }));
});
