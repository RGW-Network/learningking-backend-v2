export class MessageUtils {
    /**
     * This is the maximam time in milli-seconds that a message is rendered
     */
    static MaximumErrorMessageDisplayTimeInSeconds = 10000;

    /**
     * Show success message x milliseconds.
     *
     * @param contextref
     * @param message
     */
    static showSuccessMessage = (contextref: any, message: string) => {
        contextref.current.show({ severity: "success", content: message, life: this.MaximumErrorMessageDisplayTimeInSeconds });
    };

    /**
     * Show info message x milliseconds.
     *
     * @param contextref
     * @param message
     */
    static showInfoMessage = (contextref: any, message: string) => {
        contextref.current.show({ severity: "info", content: message, life: this.MaximumErrorMessageDisplayTimeInSeconds });
    };

    /**
     * Show warning message x milliseconds.
     *
     * @param contextref
     * @param message
     */
    static showWarnMessage = (contextref: any, message: string) => {
        contextref.current.show({ severity: "warn", content: message, life: this.MaximumErrorMessageDisplayTimeInSeconds });
    };

    /**
     * Show error message for x milliseconds.
     *
     * @param contextref
     * @param message
     */
    static showErrorMessage(contextref: any, message: string) {
        contextref.current?.show({ severity: "error", content: message, life: this.MaximumErrorMessageDisplayTimeInSeconds });
    }
}
