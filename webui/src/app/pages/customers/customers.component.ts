import { Component, OnInit, OnDestroy, TemplateRef, ViewChild, HostListener } from '@angular/core';
import { ColumnMode, SelectionType   } from '@swimlane/ngx-datatable';
import { Router, NavigationEnd       } from '@angular/router';
import { CustomerService             } from '../../services/api/customer.service';
import { UserInfoService             } from '../../services/user-info.service';

import { Customer                    } from '../../models/customer';
import { CustomerManagementService   } from '../../services/api/usermanagement/customermanagement.service';


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
    
    ColumnMode = ColumnMode;
    SelectionType = SelectionType;

    editCustomerObject: Customer = {distributorId: 0, id: 0, relatedTo: null, userName: '', email: '', firstName: '', lastName: '', contact: '', altContact: '', enabled: true, phoneNumber: '', companyName: '', addressId: 0, addressLine1: '', addressLine2: '',addressLine3: '', city: '', state: '', country: '', postalCode: '', role: 'customer' } as Customer;
    customerEditModal = false;
    
    constructor(private router: Router, private customerService: CustomerService, private userInfoService: UserInfoService,  private customerManagementService: CustomerManagementService) {
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
       console.log('enableCustomer ');     
       console.log(row);      
       this.editCustomerObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: true, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'customer'} ;   
       this.saveCustomer();
    } 
    
    disableCustomer(row) {
       console.log('disableCustomer');     
       console.log(row);    
       this.editCustomerObject = {id: row.customerId, distributorId: row.userItem.distributorId, relatedTo: row.relatedTo, userName: row.userItem.userId, email: row.userItem.email.trim(), firstName: row.userItem.firstName, lastName: row.userItem.lastName, contact: row.contact, altContact: row.altContact, enabled: false, phoneNumber: row.userItem.phoneNumber, companyName: row.companyName, addressId: row.userItem.address.id, addressLine1: row.userItem.address.addressLine1, addressLine2: row.userItem.address.addressLine2, addressLine3: row.userItem.address.addressLine3, city: row.userItem.address.city, state: row.userItem.address.state, country: row.userItem.address.country, postalCode: row.userItem.address.postalCode, role: 'customer'} ;   
       this.saveCustomer();
    } 
    
    createDelivery(row) {
       console.log('create delivery');     
       console.log(row);  
    }  
    
    /* Customer dialog */    
    public deleteCustomer(row) {       
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
            
    toggleExpandRow(row) {
        console.log('Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
        this.isToggled = true;
        console.log('Detail Toggled', event);
    }

    onSelect({ selected }) {
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