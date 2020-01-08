import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@app/shared/security/auth.service';
import { AuthenticatedUserFacade } from '@app/shared/security/authenticated-user-facade.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-not-authenticated',
  templateUrl: './not-authenticated.component.html',
  styleUrls: ['./not-authenticated.component.scss']
})
export class NotAuthenticatedComponent implements OnInit {

  constructor(
      private readonly router: Router,
      private readonly authService: AuthService,
      private readonly authenticatedUser: AuthenticatedUserFacade) { }

  ngOnInit() {
    this.authenticatedUser.isLoggedIn().pipe(
        filter(isLoggedIn => isLoggedIn === true),
    ).subscribe({
      next: () => this.router.navigate(['/'])
    });
  }

  login() {
    this.authService.login();
  }
}
