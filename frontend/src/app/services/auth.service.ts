import {Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {LoginResponse} from "../dto/LoginResponse";
import {AuthRequest} from "../dto/AuthRequest";
import {catchError, tap} from "rxjs/operators";
import {Router} from "@angular/router";
import {RegisterRequest} from "../dto/RegisterRequest";
import {error} from "@angular/compiler/src/util";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authStatus = new BehaviorSubject<boolean>(this.getAccessToken() !== null);
  isAuthenticated$ = this.authStatus.asObservable();
  // isRegistered: boolean=false;

  constructor(private http: HttpClient, private router: Router) {
  }



  login(authRequest: AuthRequest): Observable<LoginResponse> {
    console.log(authRequest);
    return this.http.post<LoginResponse>(`http://localhost:8080/login`, authRequest).pipe(
      tap((response) => {
        if (response.accessToken) {
          this.authStatus.next(true)
        }
      })
    )
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem('refreshToken');
    console.log(refreshToken);
    return this.http.post('http://localhost:8080/refresh', {}, {
      headers: new HttpHeaders().set('Authorization', `Bearer ${refreshToken}`)
    });
  }

  getTokensFromUrl() {
    const params = new URLSearchParams(window.location.search);
    const accessToken = params.get('accessToken');
    const refreshToken = params.get('refreshToken');

    if (accessToken && refreshToken) {
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
    }
  }


  getAccessToken(): string | null {
    return localStorage.getItem('accessToken');
  }

  getRefreshToken(): string | null {
    return localStorage.getItem('refreshToken');
  }

  logout() {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.authStatus.next(false);
    this.router.navigate(['/signin']);
  }

  loginSuccess() {
    this.authStatus.next(true)
  }

  changeAuthStatus() {
    this.authStatus.next(!!this.getAccessToken())
  }

  register(userData: RegisterRequest): Observable<void> {
    console.log('before register request');
    return this.http.post<void>('http://localhost:8080/registration', userData).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 400) {
          console.log(error.error)
          return throwError(error.error)
        }
        return throwError('Щось пішло не так, спробуйте ще раз.');
      })
    );
  }
  
}
