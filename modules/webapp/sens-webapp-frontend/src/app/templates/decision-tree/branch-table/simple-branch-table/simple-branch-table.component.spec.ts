import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../../test/test.module';
import { BranchTableModule } from '../branch-table.module';
import { SimpleBranchTableComponent } from './simple-branch-table.component';

describe('SimpleBranchTableComponent', () => {
  let component: SimpleBranchTableComponent;
  let fixture: ComponentFixture<SimpleBranchTableComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchTableModule
          ]
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SimpleBranchTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
