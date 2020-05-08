import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from '@app/app.component';
import { BriefMessageComponent } from '@app/components/brief-message/brief-message.component';
import { ErrorWindowComponent } from '@app/components/communication-error/error-window.component';
import { TestModule } from '@app/test/test.module';
import { AppBarModule } from './app-bar/app-bar.module';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        TestModule,
        AppBarModule
      ],
      declarations: [
        AppComponent,
        ErrorWindowComponent,
        BriefMessageComponent
      ],
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });
});
