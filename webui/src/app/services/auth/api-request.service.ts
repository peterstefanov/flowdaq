import { Injectable, Inject } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpRequest,  HttpParams } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable} from 'rxjs';
import { catchError } from 'rxjs/operators';
import { UserInfoService, LoginInfoInStorage} from '../user-info.service';
import { AppConfig } from '../../app-config';
import { map } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable()
export class ApiRequestService {

    constructor(
        private appConfig:AppConfig,
        private http: HttpClient,
        private router:Router,
        private userInfoService:UserInfoService
    ) {}

    /**
     * This is a Global place to add all the request headers for every REST calls
     */
    getHeaders():HttpHeaders {
        let headers = new HttpHeaders();     
        let token = this.userInfoService.getStoredToken();
        headers = headers.append('Content-Type', 'application/json');
        if (token !== null) {
            headers = headers.append("Authorization", "Bearer " + token);
        }
        return headers;
    }

    getNoParams(url:string):Observable<any>{
        let me = this;
        return this.http.get(this.appConfig.baseApiPath + url, {headers:this.getHeaders()} )
            .pipe(
            catchError(function(error:any){
                console.log("GET Some error in catch!!");
                console.log(error);
                if (error.status === 401 || error.status === 403){                 
                    if (error.error && error.error.operationStatus === 'TOKEN_EXIPRED') {
                        me.router.navigate(['/logout']);
                    } else {
                        me.router.navigate(['/login']);
                    }                 
                }
                if (error.status === 500){
                    me.router.navigate(['/logout']);
                }
                return throwError(error || 'Server error')
            }));
    }
    
    get(url:string, urlParams?:HttpParams):Observable<any>{
        let me = this;
        return this.http.get(this.appConfig.baseApiPath + url, {headers:this.getHeaders(),  params:urlParams} )
            .pipe(
            catchError(function(error:any){
                console.log("GET Some error in catch!!");
                console.log(error);
                if (error.status === 401 || error.status === 403){                 
                    if (error.error && error.error.operationStatus === 'TOKEN_EXIPRED') {
                        me.router.navigate(['/logout']);
                    } else {
                        me.router.navigate(['/login']);
                    }                 
                }
                if (error.status === 500){
                    me.router.navigate(['/logout']);
                }
                return throwError(error || 'Server error')
            }));
    }

    post(url:string, body:Object):Observable<any>{
        let me = this;
        return this.http.post(this.appConfig.baseApiPath + url, JSON.stringify(body), { headers:this.getHeaders()}).pipe((map(response => {console.log('POST RESPONSE'); console.log(response); return response;})));
    }

    put(url:string, body:Object):Observable<any>{

        let me = this;
        return this.http.put(this.appConfig.baseApiPath + url, JSON.stringify(body), { headers:this.getHeaders()}).pipe((map(response => {console.log('PUT RESPONSE'); console.log(response); return response;})));
    }

    delete(url:string):Observable<any>{
        let me = this;
        return this.http.delete(this.appConfig.baseApiPath + url, { headers:this.getHeaders()})
        .pipe(
            catchError(function(error:any){
            console.log("DELETE Some error in catch!!");
            console.log(error);
                if (error.status === 401){
                    if (error.error && error.error.operationStatus === 'TOKEN_EXIPRED') {
                        me.router.navigate(['/logout']);
                    } else {
                        me.router.navigate(['/login']);
                    } 
                }
                if (error.status === 500){
                    me.router.navigate(['/logout']);
                }
                return throwError(error || 'Server error')
            }));
    }

}
