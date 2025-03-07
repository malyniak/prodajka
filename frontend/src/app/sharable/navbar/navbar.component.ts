import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  isAuthenticated$!: Observable<boolean>

  constructor(private authService: AuthService) {
  }

  ngOnInit(): void {
    this.isAuthenticated$ = this.authService.isAuthenticated$
  }

  logout() {
    console.log('logout');
    this.authService.logout();
  }
}
