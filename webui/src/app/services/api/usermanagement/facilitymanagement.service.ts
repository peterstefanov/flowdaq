import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../../auth/api-request.service';
import { Facility                            } from '../../../models/facility';


export interface CustomerResponse{
    success:boolean;
    message:string;
}

@Injectable()
export class FacilityManagementService {

    constructor(
        private router: Router,
        private apiRequest: ApiRequestService
    ) {}


    createFacility(facility: Facility): Observable < any > {
        let me = this;

        let facilityDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let facilityResponse: CustomerResponse;

        this.apiRequest.post('api/usermanagement/facility', facility)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    facilityResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    facilityResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                facilityDataSubject.next(facilityResponse);
            },
            err => {
                facilityResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                facilityDataSubject.next(facilityResponse);
            });

        return facilityDataSubject;
    }
    
    updateFacility(facility: Facility): Observable < any > {
        let me = this;

        let facilityDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let facilityResponse: CustomerResponse;

        this.apiRequest.put('api/usermanagement/facility', facility)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    facilityResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    facilityResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                facilityDataSubject.next(facilityResponse);
            },
            err => {
                facilityResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                facilityDataSubject.next(facilityResponse);
            });

        return facilityDataSubject;
    }
    
    deleteFacility(facilityId): Observable < any > {
        let me = this;

        let facilityDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let facilityResponse: CustomerResponse;

        this.apiRequest.delete('api/usermanagement/facility/' + facilityId)
            .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    facilityResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    facilityResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                facilityDataSubject.next(facilityResponse);
            },
            err => {
                facilityResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                facilityDataSubject.next(facilityResponse);
            });

        return facilityDataSubject;
    }
}