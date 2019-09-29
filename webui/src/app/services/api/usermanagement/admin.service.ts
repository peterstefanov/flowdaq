import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../../auth/api-request.service';
import { Admin                               } from '../../../models/admin';


export interface CreateAdminResponse{
    success:boolean;
    message:string;
}

@Injectable()
export class AdminService {

    constructor(
        private router: Router,
        private apiRequest: ApiRequestService
    ) {}


    createAdmin(admin: Admin): Observable < any > {
        let me = this;

        let adminDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let createAdminResponse: CreateAdminResponse;

        this.apiRequest.post('api/usermanagement/admin', admin)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    createAdminResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    createAdminResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                adminDataSubject.next(createAdminResponse);
            },
            err => {
                createAdminResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                adminDataSubject.next(createAdminResponse);
            });

        return adminDataSubject;
    }
}