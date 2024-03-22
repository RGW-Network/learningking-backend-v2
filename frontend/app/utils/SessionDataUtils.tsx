/**
 * Save data to the local storage.
 * 
 * @param keyName 
 * @param value 
 * @returns 
 */
export function setSessionData(keyName: string, value: any) {
    if(value == null)
        return;
    localStorage.setItem(keyName, JSON.stringify(value))
}
/**
 * Get data from local storage.
 * 
 * @param keyName 
 * @returns 
 */
export function getSessionData(keyName: string) {
    if(localStorage.getItem(keyName))
        return JSON.parse(localStorage.getItem(keyName)!);
    return {};
}

/**
 * This removes a defined session data variable from local storage if it already exists
 * using the localStorage.removeItem() method. To be used in scenarios of resetting 
 * session variables
 * @param keyName 
 */
export function clearSessionDataVariable(keyName: string) {
    if (localStorage.getItem(keyName))
        localStorage.removeItem(keyName);
}