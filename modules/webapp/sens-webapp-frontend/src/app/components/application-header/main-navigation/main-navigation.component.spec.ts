import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { EMPTY } from 'rxjs';
import { AuthService } from '../../../shared/auth/auth.service';
import { InboxService } from '../../../templates/inbox/inbox.service';
import { TestModule } from '../../../test/test.module';
import { ApplicationHeaderModule } from '../application-header.module';

import { MainNavigationComponent } from './main-navigation.component';

describe('MainNavigationComponent', () => {
  let component: MainNavigationComponent;
  let fixture: ComponentFixture<MainNavigationComponent>;

  let authService: AuthService;
  let inboxService: InboxService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [TestModule, ApplicationHeaderModule]
        })
        .compileComponents();

    inboxService = TestBed.get(InboxService);
    authService = TestBed.get(AuthService);

    spyOn(inboxService, 'getStats').and.returnValue(EMPTY);
  }));

  function initComponent() {
    fixture = TestBed.createComponent(MainNavigationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  }

  it('should create', () => {
    initComponent();

    expect(component).toBeTruthy();
  });

  it('should call logout on authService when invoke logout', () => {
    spyOn(authService, 'logout');

    initComponent();
    component.logout();

    expect(authService.logout).toHaveBeenCalled();
  });

  [
    {property: 'hasDecisionTreeAccess', url: '/decision-tree'},
    {property: 'hasWorkflowManagementAccess', url: '/workflow-management'},
    {property: 'hasUserManagementAccess', url: '/user-management'},
    {property: 'hasAuditTrailAccess', url: '/audit-trail'},
  ].forEach(c => {
    it(`should ${c.property} be true if has access to ${c.url}`, () => {
      spyOn(authService, 'hasAccessToUrl').and.callFake(url => url === c.url);

      initComponent();

      expect(component[c.property]).toBeTruthy();
    });
  });

});
