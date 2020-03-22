import { Injectable, Inject } from '@angular/core';
import { Observable, ReplaySubject, Subject } from 'rxjs';
import { ApiRequestService } from '../auth/api-request.service';
import { HttpParams} from "@angular/common/http";


@Injectable()
export class DeviceService {

    constructor(
        private apiRequest: ApiRequestService
    ) {}


    getDevices(customerId): Observable < any > {
        let me = this;

        let deviceListSubject = new Subject < any > ();

        this.apiRequest.getNoParams('api/devices/' + customerId)
        .subscribe(jsonResp => {
            let itemsDisplay = jsonResp.items.map(function(value, i, a) {
                let newRow = Object.assign({}, value, {
                    companyName: value.companyName,
                    full: value.full,
                    empty: value.empty,
                    max: value.max,
                    capacity: value.capacity,
                    deliveryDate: value.deliveryDate,
                    count: value.count
                });
                return newRow;
            });

            let returnObj = Object.assign({}, jsonResp, {items: itemsDisplay})
            deviceListSubject.next(returnObj);
        });

        return deviceListSubject;
    }

}