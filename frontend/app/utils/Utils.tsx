import { DateTime } from 'luxon';
import { EmailRegex, SupportedTerritories } from '../constants/Constants';
import { UserSessionUtils } from './UserSessionUtils';
import { isValidPhoneNumber, parsePhoneNumber } from 'libphonenumber-js';
import { FAILED_TO_LOAD_MESSAGE, INTERNAL_SERVER_ERROR } from '../constants/ErrorMessages';

/**
 * This formats a date from the Calendar object into the yyyy-mm-dd format
 * @param {*} date
 * @returns
 */
export function formatJSDate(date: any, showTime: boolean = false) {
    if (date == null) return null;
    if (!showTime) {
        var d = new Date(date),
            month = '' + (d.getMonth() + 1),
            day = '' + d.getDate(),
            year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    } else {
        return new Date(date).toISOString();
    }
}

/**
 * This converts a number to the en-us locale to put commas in the string
 *
 * @param {Number} amount
 * @returns
 */
export function formatAmountWithCommas(amount: Number, currency: string | null) {
    if (amount) {
        var formattedAmount = amount.toLocaleString();
        return currency == null ? formattedAmount : currency + ' ' + formattedAmount;
    } else return '-';
}

/**
 * This converts a number to the en-us locale to put commas in the number
 *
 * @param {Number} number
 * @returns {String}
 */
export function formatNumberWithCommas(number: Number, defaultNumber: Number | string | null = '-') {
    if (number) {
        return number.toLocaleString();
    } else {
        return defaultNumber;
    }
}

/**
 * This converts a string of a date time instance to a date time string using the
 * luxon DateTime library
 *
 * @param {string} date_value
 * @returns {string}
 */
export function toReadableDate(date_value: string, date_only: boolean = false) {
    if (date_value == null) {
        return '-';
    } else if (date_value && date_only) {
        return DateTime.fromISO(date_value).toLocaleString(DateTime.DATE_MED_WITH_WEEKDAY);
    } else if (date_value && !date_only) {
        return DateTime.fromISO(date_value).toLocaleString(DateTime.DATETIME_MED_WITH_WEEKDAY);
    }
}

/**
 * This returns the current date time into an ISO string
 */
export function getCurrentDateTimeISOFormat() {
    return DateTime.now().toISO();
}

/**
 * This returns the first date of the current month
 */
export function getFirstDateOfMonth() {
    return DateTime.local().startOf('month').toISODate();
}

/**
 * This returns the current date into an ISO string
 */
export function getCurrentDateISOFormat() {
    return DateTime.now().toISODate();
}

export function getDateFromString(dateString: string) {
    return DateTime.fromISO(dateString);
}

/**
 * Check if the dictionary is empty
 * @param obj
 * @returns
 */
export function isDictEmpty(obj: {}) {
    return Object.keys(obj).length === 0;
}

export function replaceWithUnderscore(value: string) {
    return value.replace(/\s+/g, '_').toLowerCase();
}

/**
 * This converts a boolean to a Yes or No string
 *
 * @param {Boolean} field
 * @returns
 */
export function formatBooleanFields(field: Boolean) {
    if (field === null) return '-';
    else if (field === true) return 'Yes';
    else if (field === false) return 'No';
}

export function sanitizeValue(recordValue: any) {
    return recordValue === null || recordValue === undefined ? '-' : recordValue;
}

/**
 * Generate random Id to be used as a device Id.
 *
 * @param length
 * @returns
 */
export function generateId(length: number) {
    var result = '';
    var characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    var charactersLength = characters.length;
    for (var i = 0; i < length; i++) {
        result += characters.charAt(Math.floor(Math.random() * charactersLength));
    }
    return result;
}

/**
 * This formats a string to replace string contents with the arguments index e.g.
 * formatString("{0} is {1} and {2}", "This", "Great", "Wonderful") would return
 * "This is Great and Wonderful".
 */
export function formatString(str: string, ...replacements: string[]) {
    for (let index = 0; index < replacements.length; index++) {
        str = str.replace(`{${index}}`, replacements[index]);
    }
    return str;
}

/**
 * This splits the supplied string by commas and returns a valid string
 * @param {string} commaSeparatedString
 * @returns {Array} an array of strings
 */
export function splitCommaSeparatedString(commaSeparatedString: string) {
    if (commaSeparatedString !== null) {
        return commaSeparatedString.split(',').map((item) => {
            return item.trim();
        });
    }
    return [];
}

/**
 * This validates a string using the libphonenumber js lib through the supported territories
 *
 * @param {string} phoneNumber
 * @returns
 */
export function validatePhoneNumber(phoneNumber: string) {
    var result = false;
    SupportedTerritories.forEach((territory: any) => {
        if (isValidPhoneNumber(phoneNumber, territory.countryIsoCode)) {
            result = true;
        }
    });
    return result;
}

/**
 * This adds a plus character to a phone number string after checking if the first character is a +
 *
 * @param {string} phoneNumber
 */
export function addPlusCharacterToPhoneNumber(phoneNumber: string) {
    if (phoneNumber.charAt(0) !== '+') {
        return '+' + phoneNumber;
    }
    return phoneNumber;
}

/**
 * This validates a string using the libphonenumber js lib to make sure it is in an international format
 *
 * @param {string} phoneNumber
 */
export function validateInternationalPhoneNumber(phoneNumber: string) {
    var result = false;
    try {
        var parsedPhoneNumber = parsePhoneNumber(addPlusCharacterToPhoneNumber(phoneNumber));
        if (parsedPhoneNumber != null && SupportedTerritories.filter((e) => e.countryIsoCode === parsedPhoneNumber.country).length > 0 && parsedPhoneNumber.isValid()) {
            result = true;
        }
    } catch (error) {
        result = false;
    }
    return result;
}

/**
 * This validates an email string using a regular expression
 *
 * @param {string} email
 * @returns
 */
export function validateEmail(email: string) {
    return email.match(EmailRegex);
}

/**
 * This returns the value from a key value list of dictionaries
 * @param arr
 * @param key
 * @returns
 */
export function getArrayKeyValue(arr: any, key: string) {
    let value: number | string | null = null;

    for (const element of arr) {
        if (element?.key === key) {
            value = element?.value;
            break;
        }
    }

    return value;
}

/**
 * This checks whether a user has a specific permission
 * @param permission
 * @returns boolean
 */
export function userHasPermission(permissionName: string) {
    let userDetails: any = UserSessionUtils.getUserDetails();
    if (userDetails?.isASuperAdmin) {
        return true;
    }
    return userDetails?.permissionLists?.some((userPermission: any) => userPermission.permissionName === permissionName);
}

/**
 * This checks whether a user has a specific permission using the permission Id. This is better since it is an enum
 * value in the back office. Use a combination of permission Id and Module
 * @param permission
 * @returns boolean
 */
export function userHasPermissionId(module: number, permissionId: number) {
    let userDetails: any = UserSessionUtils.getUserDetails();
    if (userDetails?.isASuperAdmin) {
        return true;
    }
    return userDetails?.permissionLists?.some((userPermission: any) => userPermission.module === module && userPermission.permissionId === permissionId);
}

/**
 * This checks whether a user has a list of permissions by cross referencing the module id and permission id.
 * It uses the permission id and module id because they are enum values in the back office with least likely
 * chance to change.
 * @param module
 * @param permissions
 * @returns
 */
export function userHasModulePermissions(module: number, permissions: any) {
    let userDetails: any = UserSessionUtils.getUserDetails();
    if (userDetails?.isASuperAdmin) {
        return true;
    }
    return userPermissionsContains(userDetails?.permissionLists, module, permissions);
}

/**
 * This checks whether a user is allowed to see a certain module
 * @param moduleName
 * @returns boolean
 */
export function userHasModule(moduleName: string) {
    let userDetails: any = UserSessionUtils.getUserDetails();
    if (userDetails?.isASuperAdmin) {
        return true;
    }
    let userModules: any = [userDetails?.permissionLists?.map((permission: any) => permission.moduleName)];
    return userModules?.some((userModule: any) => userModule === moduleName);
}

/**
 * This checks wheter a user has a list of modules
 * @param moduleIds
 * @returns
 */
export function userHasModuleIds(moduleIds: any) {
    let userDetails: any = UserSessionUtils.getUserDetails();
    if (userDetails?.isASuperAdmin) {
        return true;
    }
    let userModules: any = [userDetails?.permissionLists?.map((permission: any) => permission.module)];
    return userModules?.some((userModule: any) => moduleIds.includes(userModule));
}

export function userPermissionsContains(userPermissionsList: any, module: number, permissionsArray: any) {
    return userPermissionsList?.some((userPermission: any) => userPermission.module === module && permissionsArray.includes(userPermission.permissionId));
}

export function arrayContains(arrayObject: any, value: any) {
    return arrayObject?.some((object: any) => value === object);
}

export function getGoogleMapsURL(latitude: string, longitude: string) {
    return `https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}`;
}

/**
 * This gets the default year range for the date picker to display when selecting a date in the past.
 * @returns
 */
export function getDefaultYearRange() {
    return `1900:${new Date().getFullYear()}`;
}

/**
 * This gets the year range for a datepicker when selecting a date in the past and future
 * @param numberOfYearsBefore
 * @param numberOfYearsAfter
 * @returns
 */
export function getYearRange(numberOfYearsBefore: number, numberOfYearsAfter: number) {
    return `${new Date().getFullYear() - numberOfYearsBefore}:${new Date().getFullYear() + numberOfYearsAfter}`;
}

/**
 * This converts a string to all lower cases using the typescript fn of toLowerCase
 * @param stringToConvert
 * @returns
 */
export function convertToLowerCase(stringToConvert: string | null) {
    try {
        return stringToConvert?.toLowerCase();
    } catch (error: any) {
        return stringToConvert;
    }
}

/**
 * This templates returns formated text hyperlinked to the google maps page with the supplied coordinates
 * @param data :{ latitude: number; longitude: number }
 * @returns
 */
export const googleMapsLinkTemplate = (data: any) => {
    if (data?.latitude !== null && data?.latitude !== undefined && data?.longitude !== null && data?.longitude !== undefined) {
        return <a className="hyper-link" href={getGoogleMapsURL(data?.latitude, data?.longitude)} target="_blank">{`${data?.latitude}, ${data?.longitude}`}</a>;
    }
};

/**
 * Use this function to replace the default internal server error message with a
 * specific message of the object that failed to load
 * @param error
 * @param objectName
 * @returns
 */
export const sanitizeErrorMessage = (error: any, objectName: string) => {
    return error.message === INTERNAL_SERVER_ERROR ? formatString(FAILED_TO_LOAD_MESSAGE, objectName) : error.message;
};

/**
 * Dropdown lables template for accounts
 * @param option
 * @returns
 */
export const accountLabelTemplate = (option: any) => {
    return <div>{option?.firstName + ' ' + option?.lastName}</div>;
};

/**
 *
 * @param anyValue Returns true if a given object is null or undefined
 * @returns
 */
export function isEmpty(anyValue: any) {
    if (anyValue == undefined || anyValue == null || anyValue == '' || anyValue == ' ') {
        return true;
    } else {
        return false;
    }
}

export function isNotEmpty(anyValue: any) {
    return !isEmpty(anyValue);
}
export const memberLabelTemplate = (option: any) => {
    return (
        <div>
            <span className=" font-bold"> {`${option?.firstName} ${option?.lastName}`}</span>
            <span className="ml-2 ">Phone:</span>
            <span className=" font-bold"> {option?.phoneNumber}</span>
            <span className="ml-2 ">Village:</span>
            <span className=" font-bold"> {option?.village?.value}</span>
        </div>
    );
};

export const groupLabelTemplate = (option: any) => {
    return (
        <div>
            <span className=" font-bold"> {`${option?.name} ${option?.registrationNumber}`}</span>
        </div>
    );
};

/**
 * Removes white spaces from a string
 * @param anyString
 * @returns
 */
export function removeWhiteSpaces(anyString: string) {
    if (anyString == null) {
        return null;
    }
    return anyString.replace(/\s/g, '').toLocaleLowerCase();
}

/**
 * Cleans a string into the css style class name format
 * @param anyString
 * @returns
 */
export function toStyleClassName(anyString: string) {
    if (anyString == null) {
        return null;
    }
    return removeWhiteSpaces(anyString)?.toLocaleLowerCase();
}

export const generalStatusBodyTemplate = (status: string) => {
    return <span className={`status-badge status-${toStyleClassName(status)}`}>{status}</span>;
};
