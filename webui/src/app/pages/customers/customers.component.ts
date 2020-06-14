import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { CustomerService             } from '../../services/api/customer.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Customer                    } from '../../models/customer';
import { CustomerManagementService   } from '../../services/api/usermanagement/customermanagement.service';

import { Delivery                    } from '../../models/delivery';
import { DeliveryService             } from '../../services/api/delivery.service';

@Component({
	selector: 'f-customers-pg',
	templateUrl: './customers.component.html',
    styleUrls: [ './customers.scss'],
})

export class CustomersComponent implements OnDestroy {

   @ViewChild('myTable', {static: false}) table: any;

    rows: any[];
    isLoading: boolean = false;
    navigationSubscription;
    expanded: any = {};
    timeout: any;
    isToggled: boolean = false;
    selected = [];;
    public distributorName: string = "";
    
    public customerErrorMsg: string = '';
    public customerSuccessMsg: string = '';
    
    public deliveryErrorMsg: string = '';
    public deliverySuccessMsg: string = '';
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    editCustomerObject: Customer = {distributorId: 0, id: 0, relatedTo: null, userName: '', email: '', firstName: '', lastName: '', contact: '', altContact: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'customer' } as Customer;
    customerEditModal = false;   
        
    createDeliveryObject: Delivery = {id: 0, status: '', fromDistributorId: 0, fromCustomerId: 0, fromFacilityId: 0, toCustomerId: 0, toFacilityId: 0, toCoolerId: 0, driverId: 0, vehicleId: 0, deliveryDate: new Date(), actualDeliveryDate: new Date(), fullBottles: 0, actualFullsDelivered: 0, routeId: 0, emptiesRetrieved: 0, actualEmptiesRetrieved: 0, deliveryNotes: ''} as Delivery;
    deliveryCreateModal = false;
    
    constructor(
        private router: Router, 
        private customerService: CustomerService, 
        private userInfoService: UserInfoService,  
        private customerManagementService: CustomerManagementService,
        private deliveryService: DeliveryService
    ) {
        this.navigationSubscription = this.router.events.subscribe((e: any) => {
            // If it is a NavigationEnd event re-initalise the component
            if (e instanceof NavigationEnd) {
                this.getPageData();
            }
        });
        this.distributorName = this.userInfoService.getDistributorName();
    }
    
    getPageData() {

        let me = this;
        me.isLoading = true;
        this.customerService.getCustomers(this.userInfoService.getDistributorId()).subscribe((data) => {
            me.rows = data.items;
            me.isLoading = false;
        });
    }

    onPage(event) {
        clearTimeout(this.timeout);
        this.timeout = setTimeout(() => {
            console.log('paged!', event);
        }, 100);
    }
 
    
    /**Customer action*/
    editCustomer(row) {
       this.editCustomerObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: row.userItem.enabled, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'customer'} ;
       this.customerEditModal = true;       
    }     
       
    enableCustomer(row) {    
       this.editCustomerObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: true, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'customer'} ;   
       this.saveCustomer();
    } 
    
    disableCustomer(row) {   
       this.editCustomerObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: false, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'customer'} ;   
       this.saveCustomer();
    } 
       
    /* Customer dialog */    
    public deleteCustomer(row) {      
               
       this.customerSuccessMsg = '';
       this.customerErrorMsg = ''; 
        
       this.customerManagementService.deleteCustomer(row.customerId)
           .subscribe(resp => {
               if (resp.success === false) {
                   this.customerErrorMsg = resp.message;
                   return;
               } else if (resp.success === true) {
                   this.cancelCustomer();
                   this.customerSuccessMsg = resp.message;
                   /**On success unselect the user and reload the table*/
                   this.remove();
                   this.getPageData();
                   return;
               }              
           }
       );
    }  
    
    public saveCustomer(): void {
       
        this.customerSuccessMsg = '';
        this.customerErrorMsg = '';  
        
        this.customerManagementService.updateCustomer(this.editCustomerObject)
            .subscribe(resp => {
                if (resp.success === false) {
                    this.customerErrorMsg = resp.message;
                    return;
                } else if (resp.success === true) {
                    this.cancelCustomer();
                    this.customerSuccessMsg = resp.message;
                    /**On success unselect the user and reload the table*/
                    this.remove();
                    this.getPageData();
                    return;
                 }              
             }
         );
    }

    public cancelCustomer(): void {
        this.customerEditModal = false;
    }
    
    public closeCustpmerSuccess(): void {
        this.customerSuccessMsg = '';
    }
    /* END Customer dialog */
   /** END Customer action*/
      
   /* Delivery action */
        
    createDelivery(row): void {
        this.createDeliveryObject = {id: 0, status: 'SCHEDULED', fromDistributorId: this.userInfoService.getDistributorId(), fromCustomerId: 0, fromFacilityId: 0, toCustomerId: row.customerId, toFacilityId: 0, toCoolerId: 0, driverId: 0, vehicleId: 0, deliveryDate: new Date(), actualDeliveryDate: new Date(), fullBottles: 0, actualFullsDelivered: 0, routeId: 0, emptiesRetrieved: 0, actualEmptiesRetrieved: 0, deliveryNotes: ''};
        this.deliveryCreateModal = true;
    }
     
   /* Delivery dialog */      
    public saveDelivery(): void {
        this.deliverySuccessMsg = '';
        this.deliveryErrorMsg = '';

        var deliveryDate = new Date(this.createDeliveryObject.deliveryDate);
        var deliveryDateFormatted = new Date(deliveryDate.getTime() - (deliveryDate.getTimezoneOffset() * 60000 )).toISOString().split("T")[0];
        this.createDeliveryObject.deliveryDate = new Date(deliveryDateFormatted);
        /**set actual delivery date as the intended delivery date*/
        this.createDeliveryObject.actualDeliveryDate = new Date(deliveryDateFormatted);
   
        this.deliveryService.createDelivery(this.createDeliveryObject)
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

    public cancelDelivery(): void {
        this.deliveryCreateModal = false;
    }
    
    public closeDeliverySuccess(): void {
        this.deliverySuccessMsg = '';
    }
    /* END Delivery dialog */
    /* END Delivery action */
    
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
        this.customerSuccessMsg = '';
        this.customerErrorMsg = ''; 
        this.deliverySuccessMsg = '';
        this.deliveryErrorMsg = '';
        
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
        this.userInfoService.setCustomerCompany(this.selected[0].companyName);
        this.userInfoService.setCustomerId(this.selected[0].customerId);
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