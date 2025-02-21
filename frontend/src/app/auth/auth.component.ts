import {Component, OnInit} from '@angular/core';
import {AuthService} from "../services/auth.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {tap} from "rxjs/operators";
import {Router} from "@angular/router";

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent implements OnInit {

  loginForm!: FormGroup;
  errorMessage!: string;

  constructor(private authService: AuthService,
              private formBuilder: FormBuilder,
              private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(5)]]
    })
  }

  ngOnInit(): void {
  }

  login(): void {
    if (this.loginForm.invalid) {
      console.log("Invalid email or password");
      return;
    }
    this.authService.login(this.loginForm.value)
      .pipe(
        tap(loginResponse => {
          localStorage.setItem('accessToken', loginResponse.accessToken);
          localStorage.setItem('refreshToken', loginResponse.refreshToken);
        })
      )
      .subscribe({
        next: (response) => {
          console.log("Login successful", response)
          this.router.navigate(['/']);
        },
        error: (err) => {
          if (err.status === 400) {
            this.loginForm.setValue({email: '', password: ''});
            this.errorMessage = "Invalid email or password"
          }
          console.log(err);
        }
      });
  }

}
