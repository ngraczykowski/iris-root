import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { EMPTY } from 'rxjs';
import { AuthService } from '../../shared/auth/auth.service';
import { InboxService } from '../../templates/inbox/inbox.service';
import { TestModule } from '../../test/test.module';
import { ApplicationHeaderComponent } from './application-header.component';
import { ApplicationHeaderModule } from './application-header.module';

describe('ApplicationHeaderComponent', () => {
  let component: ApplicationHeaderComponent;
  let fixture: ComponentFixture<ApplicationHeaderComponent>;

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
    spyOn(authService, 'getDisplayName').and.returnValue('displayName');
    spyOn(authService, 'getUserName').and.returnValue('userName');
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ApplicationHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
