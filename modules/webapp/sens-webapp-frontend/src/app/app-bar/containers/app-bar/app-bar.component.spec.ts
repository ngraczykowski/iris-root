import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppBarComponent } from './app-bar.component';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonModule } from '@angular/material/button';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { SharedModule } from '@app/shared/shared.module';
import { UserMenuComponent } from '@app/app-bar/components/user-menu/user-menu.component';
import { BrandHeroComponent } from '@app/app-bar/components/brand-hero/brand-hero.component';
import { TestModule } from '@app/test/test.module';
import { of } from 'rxjs';

describe('AppBarComponent', () => {
  let component: AppBarComponent;
  let fixture: ComponentFixture<AppBarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppBarComponent,
        UserMenuComponent,
        BrandHeroComponent
      ],
      imports: [
        MatToolbarModule,
        MatMenuModule,
        MatButtonModule,
        MatListModule,
        MatIconModule,
        SharedModule,
        TestModule
      ]
    });
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppBarComponent);
    component = fixture.componentInstance;
    component.userDetails = of({
      firstName: 'Test',
      lastName: 'Test'
    });
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
