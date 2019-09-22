import { Injectable, Inject } from '@angular/core';
import { Router } from '@angular/router';

import { Observable,Subject,BehaviorSubject } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { UserInfoService, LoginInfoInStorage} from '../user-info.service';
import { ApiRequestService } from './api-request.service';

export interface ValidatePasswordRequestParam {
    username: string;
    password: string;
    requestCode: string;
}

export interface CodeRequestParam {
    code: string;
}

export interface ResetInfoReturn {
    success: boolean;
    message: string;
    userId: string;
}

@Injectable()
export class ResetPasswordService {

    constructor(
        private router: Router,
        private userInfoService: UserInfoService,
        private apiRequest: ApiRequestService
    ) {}

    validatePassword(username: string, password: string, requestCode: string): Observable < any > {
        let me = this;

        let bodyData: ValidatePasswordRequestParam = {
            "username": username,
            "password": password,
            "requestCode": requestCode
        }
        let resetDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let resetInfoReturn: ResetInfoReturn;

        this.apiRequest.post('public/resetpassword/validate', bodyData)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    //Create a success object that we want to send back to login page
                    resetInfoReturn = {
                        "success": true,
                        "message": jsonResp.message,
                        "userId": jsonResp.item.userId
                    };
                } else {
                    //Create a faliure object that we want to send back to login page
                    resetInfoReturn = {
                        "success": false,
                        "message": jsonResp.message,
                        "userId": ''
                    };
                }
                resetDataSubject.next(resetInfoReturn);
            },
            err => {
                resetInfoReturn = {
                    "success": false,
                    "message": err.error.message,
                    "userId": ''
                };
                resetDataSubject.next(resetInfoReturn);
            });

        return resetDataSubject;
    }

    validateCode(code: string): Observable < any > {
        let me = this;

        let resetDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let resetInfoReturn: ResetInfoReturn;

        this.apiRequest.getNoParams('public/resetpassword/' + code)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    //Create a success object that we want to send back to login page
                    resetInfoReturn = {
                        "success": true,
                        "message": jsonResp.message,
                        "userId": jsonResp.item.userId
                    };

                } else {
                    //Create a faliure object that we want to send back to login page
                    resetInfoReturn = {
                        "success": false,
                        "message": jsonResp.message,
                        "userId": ''
                    };
                }
                resetDataSubject.next(resetInfoReturn);
            },
            err => {
                resetInfoReturn = {
                    "success": false,
                    "message": err.error.message,
                    "userId": ''
                };
                resetDataSubject.next(resetInfoReturn);
            });

        return resetDataSubject;
    }
}