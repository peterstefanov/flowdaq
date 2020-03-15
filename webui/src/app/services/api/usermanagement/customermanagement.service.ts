import { Injectable, Inject                  } from '@angular/core';
import { Router                              } from '@angular/router';

import { Observable,Subject,BehaviorSubject  } from 'rxjs';
import { catchError                          } from 'rxjs/operators';
import { ApiRequestService                   } from '../../auth/api-request.service';
import { Customer                            } from '../../../models/customer';


export interface CustomerResponse{
    success:boolean;
    message:string;
}

@Injectable()
export class CustomerManagementService {

    constructor(
        private router: Router,
        private apiRequest: ApiRequestService
    ) {}


    createCustomer(customer: Customer): Observable < any > {
        let me = this;

        let customerDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let customerResponse: CustomerResponse;

        this.apiRequest.post('api/usermanagement/customer', customer)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    customerResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    customerResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                customerDataSubject.next(customerResponse);
            },
            err => {
                customerResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                customerDataSubject.next(customerResponse);
            });

        return customerDataSubject;
    }
    
    updateCustomer(customer: Customer): Observable < any > {
        let me = this;

        let cusatomerDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let customerResponse: CustomerResponse;

        this.apiRequest.put('api/usermanagement/customer', customer)
        .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    customerResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    customerResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                cusatomerDataSubject.next(customerResponse);
            },
            err => {
                customerResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                cusatomerDataSubject.next(customerResponse);
            });

        return cusatomerDataSubject;
    }
    
    deleteCustomer(customerId): Observable < any > {
        let me = this;

        let cusatomerDataSubject: BehaviorSubject < any > = new BehaviorSubject < any > ([]);
        let customerResponse: CustomerResponse;

        this.apiRequest.delete('api/usermanagement/customer/' + customerId)
            .subscribe(jsonResp => {
                if (jsonResp !== undefined && jsonResp !== null && jsonResp.operationStatus === "SUCCESS") {
                    customerResponse = {
                        "success": true,
                        "message": jsonResp.message
                    };
                } else {
                    customerResponse = {
                        "success": false,
                        "message": jsonResp.message
                    };
                }
                cusatomerDataSubject.next(customerResponse);
            },
            err => {
                customerResponse = {
                    "success": false,
                    "message": err.error.message,
                };
                cusatomerDataSubject.next(customerResponse);
            });

        return cusatomerDataSubject;
    }
}