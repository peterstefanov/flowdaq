import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../../auth/api-request.service';
import { Distributor                         } from '../../../models/distributor';


export interface CreateDistributorResponse{
    success:boolean;
    message:string;
}

@Injectable()
export class DistributorManagementService {

    constructor(
        private router: Router,
        private apiRequest: ApiRequestService
    ) {}


    createDistributor(distributor: Distributor): Observable < any > {
        let me = this;

        let distributorDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let createDistributorResponse: CreateDistributorResponse;

        this.apiRequest.post('api/usermanagement/distributor', distributor)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    createDistributorResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    createDistributorResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                distributorDataSubject.next(createDistributorResponse);
            },
            err => {
                createDistributorResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                distributorDataSubject.next(createDistributorResponse);
            });

        return distributorDataSubject;
    }
}