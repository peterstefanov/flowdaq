import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../../auth/api-request.service';
import { Distributor                         } from '../../../models/distributor';


export interface DistributorResponse{
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
        let distributorResponse: DistributorResponse;

        this.apiRequest.post('api/usermanagement/distributor', distributor)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    distributorResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    distributorResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                distributorDataSubject.next(distributorResponse);
            },
            err => {
                distributorResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                distributorDataSubject.next(distributorResponse);
            });

        return distributorDataSubject;
    }
    
    updateDistributor(distributor: Distributor): Observable < any > {
        let me = this;

        let distributorDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let distributorResponse: DistributorResponse;

        this.apiRequest.put('api/usermanagement/distributor', distributor)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    distributorResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    distributorResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                distributorDataSubject.next(distributorResponse);
            },
            err => {
                distributorResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                distributorDataSubject.next(distributorResponse);
            });

        return distributorDataSubject;
    }
    
    deleteDistributor(distributorId): Observable < any > {
        let me = this;

        let distributorDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let distributorResponse: DistributorResponse;

        this.apiRequest.delete('api/usermanagement/distributor/' + distributorId)
            .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    distributorResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    distributorResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                distributorDataSubject.next(distributorResponse);
            },
            err => {
                distributorResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                distributorDataSubject.next(distributorResponse);
            });

        return distributorDataSubject;
    }
}