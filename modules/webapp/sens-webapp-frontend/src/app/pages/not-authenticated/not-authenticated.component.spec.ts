import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotAuthenticatedComponent } from '@app/pages/not-authenticated/not-authenticated.component';
import { TestModule } from '@app/test/test.module';

describe('NotAuthenticatedComponent', () => {
  let component: NotAuthenticatedComponent;
  let fixture: ComponentFixture<NotAuthenticatedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NotAuthenticatedComponent],
      imports: [TestModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotAuthenticatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
