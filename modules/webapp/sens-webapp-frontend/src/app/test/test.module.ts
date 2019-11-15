import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { SharedModule } from '@app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { CookieService } from 'ngx-cookie-service';
import { WINDOW_PROVIDERS } from '@app/shared/window.service';

@NgModule({
  imports: [
    TranslateModule.forRoot(),
    RouterTestingModule.withRoutes([]),
    FormsModule,
    SharedModule,
    HttpClientTestingModule
  ],
  exports: [
    TranslateModule,
    RouterModule,
    FormsModule,
    SharedModule,
  ],
  providers: [CookieService, WINDOW_PROVIDERS]
})
export class TestModule {}
