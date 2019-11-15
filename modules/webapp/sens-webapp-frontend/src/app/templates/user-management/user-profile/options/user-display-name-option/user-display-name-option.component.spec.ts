import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { Solution } from '@app/components/solution-tag/solution-tag.component';
import { SolutionSettingsService } from '@app/shared/solution-settings.service';
import { TestModule } from '@app/test/test.module';
import { BehaviorSubject } from 'rxjs';
import { UserOptionsModule } from '../user-options.module';
import { UserDisplayNameOptionComponent } from './user-display-name-option.component';

class MockSolutionSettingsService {
  settings = new BehaviorSubject<Solution[]>([
    {
      key: 'solution1',
      label: 'test',
      className: 'test'
    },
    {
      key: 'solution2',
      label: 'test',
      className: 'test'
    }
  ]);
}

describe('UserDisplayNameOptionComponent', () => {
  let component: UserDisplayNameOptionComponent;
  let fixture: ComponentFixture<UserDisplayNameOptionComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            UserOptionsModule
          ],
          providers: [{ provide: SolutionSettingsService, useClass: MockSolutionSettingsService }]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserDisplayNameOptionComponent);
    component = fixture.componentInstance;
    component.control = new FormControl('Display Name');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
