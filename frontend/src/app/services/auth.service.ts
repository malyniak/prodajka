import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {BehaviorSubject, Observable} from "rxjs";
import {LoginResponse} from "../dto/LoginResponse";
import {AuthRequest} from "../dto/AuthRequest";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private authStatus = new BehaviorSubject<boolean>(this.getAccessToken() !== null);
  isAuthenticated$ = this.authStatus.asObservable();

  constructor(private http: HttpClient) {
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

  saveTokens(accessToken: string, refreshToken: string) {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
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
    window.location.href = '/login';
  }
}
