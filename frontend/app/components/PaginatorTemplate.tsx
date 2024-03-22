import { formatNumberWithCommas } from "../utils/Utils"

 export const paginatorTemplate: any = {
    layout: 'FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown',
    CurrentPageReport: (options: any) => {
        return (
            <span style={{ color: 'var(--text-color)', userSelect: 'none', textAlign: 'center' }}>
                {options.first} - {options.last} of {formatNumberWithCommas(options.totalRecords, 0)} Record(s)
            </span>
        )        
    }
}
