import { Component, OnInit } from '@angular/core';
import { AuthService } from '@app/shared/auth/auth.service';

@Component({
  selector: 'app-analyst-home',
  templateUrl: './analyst-home.component.html',
  styleUrls: ['./analyst-home.component.scss']
})
export class AnalystHomeComponent implements OnInit {

  constructor(private authService: AuthService) { }

  ngOnInit() {
    setTimeout('window.location.reload();', 600000);
  }

  logout(): void {
    this.authService.logout();
  }

}
