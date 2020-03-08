import {
    Injectable
} from '@angular/core';

export interface UserInStorage {
    userId: string;
    email: string;
    role: string;
    displayName: string;
    token: string;
    distributorName: string;
    distributorId: number;
    customerCompany: string;
}

export interface LoginInfoInStorage {
    success: boolean;
    message: string;
    landingPage: string;
    user ? : UserInStorage;
}

@Injectable()
export class UserInfoService {

    public currentUserKey: string = "currentUser";
    public storage: Storage = sessionStorage; //switch between sessionStorage or LocalStrage 

    constructor() {}

    //Store userinfo from session storage
    storeUserInfo(userInfoString: string) {
        this.storage.setItem(this.currentUserKey, userInfoString);
    }

    //Remove userinfo from session storage
    removeUserInfo() {
        this.storage.removeItem(this.currentUserKey);
    }

    //Get userinfo from session storage
    getUserInfo(): UserInStorage | null {
        try {
            let userInfoString: string = this.storage.getItem(this.currentUserKey);
            if (userInfoString) {
                let userObj: UserInStorage = JSON.parse(this.storage.getItem(this.currentUserKey));
                return userObj;
            } else {
                return null;
            }
        } catch (e) {
            return null;
        }
    }

    isLoggedIn(): boolean {
        return this.storage.getItem(this.currentUserKey) ? true : false;
    }

    //Get User's Display name from session storage
    getUserName(): string {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.displayName
        }
        return "no-user";
    }

    //Get User's Distributor Display name from session storage
    getDistributorName(): string {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.distributorName
        }
        return "no-distributor";
    }

    //Get User's role (user/admin) from session storage
    getUserRole(): string {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.role
        }
        return "user";
    }

    getStoredToken(): string | null {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.token;
        }
        return null;
    }

    //Get User's distributor id from session storage
    getDistributorId(): number {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.distributorId
        }
        return 0;
    }

    setDistributorId(distributorId: number, distributorName: string) {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            let user = {"userId": userObj.userId, "email": userObj.email, "role": userObj.role, "displayName": userObj.displayName,
                        "distributorName": distributorName, "token": userObj.token, "distributorId": distributorId };
            this.storage.setItem(this.currentUserKey, JSON.stringify(user));
        }
    }

    setCustomerCompany(customerCompany: string) {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            let user = {"userId": userObj.userId, "email": userObj.email, "role": userObj.role, "displayName": userObj.displayName,
                        "distributorName": userObj.distributorName, "token": userObj.token, "distributorId": userObj.distributorId, "customerCompany": customerCompany };
            this.storage.setItem(this.currentUserKey, JSON.stringify(user));
        }
    }

    //Get User's Customer Company name from session storage
    getCustomerCompany(): string {
        let userObj: UserInStorage = this.getUserInfo();
        if (userObj !== null) {
            return userObj.customerCompany
        }
        return "no-customerCompany";
    }
}