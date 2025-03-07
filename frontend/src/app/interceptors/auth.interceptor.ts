import {
  HttpErrorResponse,
  HttpEvent, HttpEventType,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse
} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {BehaviorSubject, Observable, throwError} from "rxjs";
import {AuthService} from "../services/auth.service";
import {catchError, filter, switchMap, take, tap} from "rxjs/operators";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<string | null> = new BehaviorSubject<string | null>(null);

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!req.url.includes('/api/')) {
      return next.handle(req);
    }

    console.log('Intercepting request:', req.url);

    let authReq = this.addAuthHeader(req);

    return next.handle(authReq).pipe(
      catchError((error) => {
        console.log('Error intercepted:', error);

        if (error instanceof HttpErrorResponse && error.status === 401) {
          console.log('401 Unauthorized detected in interceptor');
          return this.handle401Error(req, next);
        }

        return throwError(() => error);
      }),
      // tap((event) => {
      //   console.log("tap intercept", event);
      //   if (event.type === HttpEventType.Response) {
      //     console.log('Response intercepted:', event);
      //     const accessToken = event.headers.get('Authorization');
      //     const refreshToken = event.headers.get('Refresh-Token');
      //
      //     if (accessToken && refreshToken) {
      //       console.log('Tokens received in response headers:', { accessToken, refreshToken });
      //       this.authService.getTokensFromUrl(); // Зберігаємо токени в localStorage
      //     }
      //   }
      // })
    );
  }

  private handle401Error(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    console.log('Handling 401 error...');

    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((response: any) => {
          console.log('New access token received:', response.accessToken);

          localStorage.setItem('accessToken', response.accessToken);
          this.isRefreshing = false;
          this.refreshTokenSubject.next(response.accessToken);

          return next.handle(this.addAuthHeader(req));
        }),
        catchError((err) => {
          console.log('Refresh token failed:', err);
          this.isRefreshing = false;
          this.authService.logout(); // Видаляємо токени та перенаправляємо на логін

          return throwError(() => err);
        })
      );
    } else {
      return this.refreshTokenSubject.pipe(
        filter((token) => token !== null),
        take(1),
        switchMap((token) => next.handle(this.addAuthHeader(req)))
      );
    }
  }

  private addAuthHeader(req: HttpRequest<any>): HttpRequest<any> {
    const accessToken = localStorage.getItem('accessToken');
    console.log(accessToken)

    if (accessToken) {
      return req.clone({
        headers: req.headers.set('Authorization', `Bearer ${accessToken}`),
      });
    }
    return req;
  }
}
