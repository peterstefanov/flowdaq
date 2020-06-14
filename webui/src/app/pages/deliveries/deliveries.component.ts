import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { DistributorService          } from '../../services/api/distributor.service';
import { CustomerService             } from '../../services/api/customer.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Delivery                    } from '../../models/delivery';
import { DeliveryService             } from '../../services/api/delivery.service';

@Component({
	selector: 'f-deliveries-pg',
	templateUrl: './deliveries.component.html',
    styleUrls: [ './deliveries.scss'],
})

export class DeliveriesComponent implements OnDestroy {

   @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];;
    public customerName: string = "";
    public userRole: string = "";
    public userName: string = "";
    public distributorName: string = "";
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    public deliveryErrorMsg: string = '';
    public deliverySuccessMsg: string = '';
    
    editDeliveryObject: Delivery = {id: 0, status: '', fromDistributorId: 0, fromCustomerId: 0, fromFacilityId: 0, toCustomerId: 0, toFacilityId: 0, toCoolerId: 0, driverId: 0, vehicleId: 0, deliveryDate: new Date(), actualDeliveryDate: new Date(), fullBottles: 0, actualFullsDelivered: 0, routeId: 0, emptiesRetrieved: 0, actualEmptiesRetrieved: 0, deliveryNotes: ''} as Delivery;
    deliveryEditModal = false;
    
    constructor(private router: Router, private customerService: CustomerService, private distributorService: DistributorService, private userInfoService: UserInfoService, private deliveryService: DeliveryService) {
        this.customerName = this.userInfoService.getCustomerCompany();
        this.userRole = this.userInfoService.getUserRole();
        this.userName = this.userInfoService.getUserName();
        this.distributorName = this.userInfoService.getDistributorName();
        
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
    }

    getPageData() {
        let me = this;
        me.isLoading = true;

        if (this.userRole === 'distributor') {
            this.distributorService.getDistributorsDeliveries(this.userInfoService.getDistributorId()).subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });
        } else if (this.userRole === 'customer') {
            this.customerService.getCustomerDeliveries(this.userInfoService.getCustomerId()).subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });
        } else if (this.userRole === 'facility') {
            this.customerService.getFacilityDeliveries(this.userInfoService.getCustomerId()).subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });           
        } else {
            this.customerService.getAllDeliveries().subscribe((data) => {
                me.rows = data.items;
                me.isLoading = false;
            });           
        }
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }

    /**Delivery action*/   
    editDelivery(row) {
       this.editDeliveryObject = {id: row.id, status: row.status, fromDistributorId: row.fromDistributorId, fromCustomerId: row.fromCustomerId, fromFacilityId: row.fromFacilityId, toCustomerId: row.toCustomerId, toFacilityId: row.toFacilityId, toCoolerId: row.toCoolerId, driverId: row.driverId, vehicleId: row.vehicleId, deliveryDate: new Date(row.deliveryDate), actualDeliveryDate: new Date(row.actualDeliveryDate), fullBottles: row.fullBottles, actualFullsDelivered: row.actualFullsDelivered, routeId: row.routeId, emptiesRetrieved: row.emptiesRetrieved, actualEmptiesRetrieved: row.actualEmptiesRetrieved, deliveryNotes: row.deliveryNotes};
       this.deliveryEditModal = true;       
    }     
 
    
    /* Delivery dialog */    
    public deleteDelivery(row) {   
       this.deliverySuccessMsg = '';
       this.deliveryErrorMsg = '';    
        
       this.deliveryService.deleteDelivery(row.id)
           .subscribe(resp => {
               if (resp.success === false) {
                   this.deliveryErrorMsg = resp.message;
                   return;
               } else if (resp.success === true) {
                   this.cancelDelivery();
                   this.deliverySuccessMsg = resp.message;
                   /**On success unselect the user and reload the table*/
                   this.remove();
                   this.getPageData();
                   return;
               }              
           }
       );
    }  
    
    public saveDelivery(): void {
        this.deliverySuccessMsg = '';
        this.deliveryErrorMsg = '';
        
        var deliveryDate = new Date(this.editDeliveryObject.deliveryDate);
        var deliveryDateFormatted = new Date(deliveryDate.getTime() - (deliveryDate.getTimezoneOffset() * 60000 )).toISOString().split("T")[0];
        this.editDeliveryObject.deliveryDate = new Date(deliveryDateFormatted);

        var actualDeliveryDate = new Date(this.editDeliveryObject.actualDeliveryDate);
        var actualDeliveryDateFormatted = new Date(actualDeliveryDate.getTime() - (actualDeliveryDate.getTimezoneOffset() * 60000 )).toISOString().split("T")[0];
        this.editDeliveryObject.actualDeliveryDate = new Date(actualDeliveryDateFormatted);
        
        this.deliveryService.updateDelivery(this.editDeliveryObject)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.deliveryErrorMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.cancelDelivery();
                    this.deliveryErrorMsg = resp.message;
                    /**On success unselect the user and reload the table*/
                    this.remove();
                    this.getPageData();
                    return;
                 }              
             }
         );
    }

    public cancelDelivery(): void {
        this.deliveryEditModal = false;
    }
    
    public closeDeliverySuccess(): void {
        this.deliverySuccessMsg = '';
    }
    /* END Delivery dialog */
    /** END Delivery action*/
            
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        console.log('Select Event', selected, this.selected);
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    remove() {
        this.selected = [];
    }
    
    onActivate(event) {
       console.log('Activate Event', event);
       if(event.type === 'click') {
           this.table.rowDetail.toggleExpandRow(event.row);
       }    
    }
    
    ngOnDestroy() {
        if (this.navigationSubscription) {
            this.navigationSubscription.unsubscribe();
        }
    }
}