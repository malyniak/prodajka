import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../services/auth.service";

@Component({
  selector: 'app-login-callback',
  template: `<p>Авторизація успішна! Перенаправлення...</p>`
})
export class LoginCallbackComponent implements OnInit {
  constructor(private route: ActivatedRoute, private router: Router,
              private authService: AuthService,) {
  }

  ngOnInit() {
    this.authService.getTokensFromUrl()
    this.authService.loginSuccess()
    this.router.navigate(['/'])
  }
}
