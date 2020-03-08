import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { UserInfoService, LoginInfoInStorage } from '../user-info.service';
import { ApiRequestService                   } from './api-request.service';

export interface LoginRequestParam{
    username:string;
    password:string;
}

export interface ResetRequestParam{
    email:string;
}

export interface ResetInfoReturn{
    success:boolean;
    message:string;
}

@Injectable()
export class LoginService {

    public landingPage: string = "/home/customers";
    public landingPageAdmin: string = "/home/distributors";
    public landingPageCustomer: string = "/home/facilities";
    
    constructor(
        private router: Router,
        private userInfoService: UserInfoService,
        private apiRequest: ApiRequestService
    ) {}

    requestResetPassword(email: string): Observable < any > {
        let me = this;

        let bodyData: ResetRequestParam = {
            "email": email
        }
        let resetDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let resetInfoReturn: ResetInfoReturn;

        this.apiRequest.post('public/forgotpassword', bodyData)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    //Create a success object that we want to send back to login page
                    resetInfoReturn = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    //Create a faliure object that we want to send back to login page
                    resetInfoReturn = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                resetDataSubject.next(resetInfoReturn);
            },
            err => {
                resetInfoReturn = {
                    "success": false,
                    "message": err.error.message,
                };
                resetDataSubject.next(resetInfoReturn);
            });

        return resetDataSubject;
    }

    getToken(username: string, password: string): Observable < any > {
        let me = this;

        let bodyData: LoginRequestParam = {
            "username": username,
            "password": password,
        }

        let loginDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let loginInfoReturn: LoginInfoInStorage;

        this.apiRequest.post('public/auth', bodyData)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    
                    let landingPage = '';
                    if(jsonResp.item.role === 'admin') {
                        landingPage = this.landingPageAdmin;
                    } else if (jsonResp.item.role === 'distributor') {
                        landingPage = this.landingPage;
                    } else if (jsonResp.item.role === 'customer') {
                        landingPage = this.landingPageCustomer;
                    }
                    
                    loginInfoReturn = {
                        "success": true,
                        "message": jsonResp.message,
                        "landingPage": landingPage,
                        "user": {
                            "userId": jsonResp.item.userId,
                            "email": jsonResp.item.email,
                            "role": jsonResp.item.role,
                            "displayName": jsonResp.item.firstName + " " + jsonResp.item.lastName,
                            "distributorName": jsonResp.item.distributorName,
                            "token": jsonResp.item.token,
                            "distributorId": jsonResp.item.distributorId
                        }
                    };

                    // store username and jwt token in session storage to keep user logged in between page refreshes
                    this.userInfoService.storeUserInfo(JSON.stringify(loginInfoReturn.user));
                } else {
                    //Create a faliure object that we want to send back to login page
                    loginInfoReturn = {
                        "success": false,
                        "message": jsonResp.message,
                        "landingPage": "/login"
                    };
                }
                loginDataSubject.next(loginInfoReturn);
            },
            err => {
                loginInfoReturn = {
                    "success": false,
                    "message": err.error.message,
                    "landingPage": "/login"
                };
                loginDataSubject.next(loginInfoReturn);
            });


        return loginDataSubject;
    }

    logout(navigatetoLogout = true): void {
        // clear token remove user from local storage to log user out
        this.userInfoService.removeUserInfo();
        if (navigatetoLogout) {
            this.router.navigate(["logout"]);
        }
    }
}