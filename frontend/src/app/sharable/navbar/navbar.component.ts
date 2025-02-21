import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/auth.service";
import {BehaviorSubject} from "rxjs";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  isAuthenticated$ = this.authService.isAuthenticated$; // Використовуємо глобальний стан

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    }

  logout() {
    this.authService.logout(); // Викликаємо метод із сервісу
  }
}
