import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { MatchRow } from '@app/templates/alert/match-table-data-mapper';
import { TestModule } from '@app/test/test.module';
import { EMPTY, of } from 'rxjs';

import { AlertComponent } from './alert.component';
import { AlertModule } from './alert.module';
import { AlertService } from './alert.service';

describe('AlertComponent', () => {
  let component: AlertComponent;
  let fixture: ComponentFixture<AlertComponent>;

  let alertService: AlertService;
  let activatedRoute: ActivatedRoute;

  const mockRouter = {
    navigate: jasmine.createSpy('navigate')
  };

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, AlertModule],
          providers: [
            {
              provide: ActivatedRoute,
              useValue: {
                params: of({decisionTreeId: 1, matchGroupId: 1, externalId: 'externalId'})
              }
            },
            {
              provide: Router, useValue: mockRouter
            }
          ]
        })
        .compileComponents();

    activatedRoute = TestBed.get(ActivatedRoute);
    alertService = TestBed.get(AlertService);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(AlertComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    spyOn(alertService, 'getAlertDetails').and.returnValue(EMPTY);

    initComponent();

    expect(component).toBeTruthy();
  });

  it('should open Branch in the same window when goToBranch', () => {
    spyOn(alertService, 'getAlertDetails').and.returnValue(EMPTY);

    initComponent();

    component.routeParams.decisionTreeId = 1;

    component.goToBranch(<MatchRow>{matchGroupId: 2});

    expect (mockRouter.navigate).toHaveBeenCalledWith (['/decision-tree/1/reasoning-branch/2']);
  });
});
