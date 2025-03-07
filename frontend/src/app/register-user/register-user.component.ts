import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../services/auth.service";
import {RegisterRequest} from "../dto/RegisterRequest";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.scss']
})
export class RegisterUserComponent implements OnInit {

  registerForm!: FormGroup;
  errorMessage!: string;
  validationErrors: any

  constructor(private fb: FormBuilder,
              private authService: AuthService,
              private router: Router) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      confirmPassword: ['', Validators.required],
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      phoneNumber: ['', [Validators.required]]
    })
  }

  register() {
    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        console.log('Successfully registered');
        this.router.navigate(['/']);
      },
      error: (errors) => {
        if (typeof errors === 'object') {
          this.validationErrors = errors; // Зберігаємо помилки у вигляді об'єкта
        } else {
          this.validationErrors.general = errors; // Якщо помилка загальна (не об'єкт)
        }
        console.log(this.validationErrors);
      }
    });
  }
  
}
