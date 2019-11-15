import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { ApplicationHeaderComponent } from '@app/components/application-header/application-header.component';
import { MainNavigationComponent } from '@app/components/application-header/main-navigation/main-navigation.component';
import { UserSectionComponent } from '@app/components/application-header/user-section/user-section.component';

import { InternalComponent } from '@app/layout/internal/internal.component';
import { AuthService } from '@app/shared/auth/auth.service';
import { InboxModule } from '@app/templates/inbox/inbox.module';
import { InboxService } from '@app/templates/inbox/inbox.service';
import { TestModule } from '@app/test/test.module';
import { EMPTY } from 'rxjs';

describe('InternalComponent', () => {
  let component: InternalComponent;
  let fixture: ComponentFixture<InternalComponent>;

  let authService: AuthService;
  let inboxService: InboxService;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          declarations: [
            InternalComponent,
            MainNavigationComponent,
            ApplicationHeaderComponent,
            UserSectionComponent
          ],
          imports: [TestModule, InboxModule]
        })
        .compileComponents();

    authService = TestBed.get(AuthService);
    inboxService = TestBed.get(InboxService);

    spyOn(inboxService, 'getStats').and.returnValue(EMPTY);
    spyOn(authService, 'getDisplayName').and.returnValue('displayName');
    spyOn(authService, 'getUserName').and.returnValue('userName');
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InternalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
