import { Component, OnInit } from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators} from "@angular/forms";
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

    this.validationErrors = {}; 
  }

  register() {
    const password = this.registerForm.get('password')?.value
    const confirmPassword = this.registerForm.get('confirmPassword')?.value
    if( password !== confirmPassword) {
      this.validationErrors = {passwordMismatch : 'Паролі не співпадають'}
      console.log(this.validationErrors)
      return 
    }
    this.authService.register(this.registerForm.value).subscribe({
      next: () => {
        console.log('Successfully registered');
        this.router.navigate(['/']);
      },
      error: (errors) => {
        console.log('error method')
        console.log(this.registerForm.value)
        if (typeof errors === 'object') {
          this.validationErrors = errors;
        } else {
          this.validationErrors.general = errors;
        }
        console.log(this.validationErrors);
      }
    });
  }

  passwordEqualsValidator(group: AbstractControl): ValidationErrors | null {
    const password = group.get('password')?.value
    const confirmPassword = group.get('confirmPassword')?.value
    return password === confirmPassword ? null : {passwordMismatch : true}
  }
  
  
}
