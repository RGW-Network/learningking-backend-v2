import { PanelHeaderTemplateOptions } from "primereact/panel";
import { Ripple } from "primereact/ripple";
import { TITLE_FILTER_HEADER } from "../constants/Constants";

/**
 * This is the header template for the filters panel
 * @param options 
 * @returns 
 */
export const filtersHeadertemplate = (options: PanelHeaderTemplateOptions) => {
    const toggleIcon = options.collapsed ? 'pi pi-chevron-down' : 'pi pi-chevron-up';
    const className = `${options.className} justify-content-start`;
    const titleClassName = `${options.titleClassName} pl-1`;

    return (
        <div className={className}>
            <button className={options.togglerClassName} onClick={options.onTogglerClick}>
                <span className={toggleIcon}></span>
                <Ripple />
            </button>
            <span className={titleClassName}>
                {TITLE_FILTER_HEADER}
            </span>
        </div>
    )
};