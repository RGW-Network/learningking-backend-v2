import { InputText } from "primereact/inputtext";
import { InputNumber } from "primereact/inputnumber";
import { Calendar } from "primereact/calendar";
import { Dropdown } from "primereact/dropdown";
import { SEARCH_DATE_FILTER_FORMAT } from "../constants/Constants";
import { formatJSDate } from "../utils/Utils";
import { MultiSelect } from "primereact/multiselect";
import { Checkbox } from "primereact/checkbox";

/**
 * This returns the component to render in the filter form. It switches between
 * a dropdown, text and date.
 * @param {*} filter
 * @returns
 */
export function getFilterComponent(filter: any) {
  if (filter.type === "dropdown") {
    return <Dropdown id={filter.id} value={filter.value} options={filter.options} optionLabel={filter.optionLabel} optionValue={filter.optionValue} onChange={(e) => filter.onChangeFn(e.value)} />;
  } else if (filter.type === "dropdown-filter") {
    return (
      <Dropdown
        id={filter.id}
        value={filter.value}
        options={filter.options}
        optionLabel={filter.optionLabel}
        filter
        showClear
        filterBy={filter.optionLabel}
        onFilter={(e) => filter.onFilter(e)}
        optionValue={filter.optionValue}
        onChange={(e) => filter.onChangeFn(e.value)}
      />
    );
  } else if (filter.type === "multiselect") {
    return (
      <MultiSelect
        optionLabel={filter.optionLabel}
        optionValue={filter.optionValue}
        value={filter.value}
        options={filter.options}
        onChange={(e) => filter.onChangeFn(e.value)}
        maxSelectedLabels={2}
        display="chip"
      />
    );
  } else if (filter.type === "text") {
    return (
      <InputText
        id={filter.id}
        type="search"
        value={filter.value || ""}
        onInput={(e) => filter.onChangeFn(e.currentTarget.value)}
        onKeyDown={(e) => {
          if (e.key === "Enter") {
            filter.onkeydownFn(e.currentTarget.value);
          }
        }}
      />
    );
  } else if (filter.type === "number") {
    return <InputNumber id={filter.id} value={filter.value || ""} onValueChange={(e) => filter.onChangeFn(e.target.value)} />;
  } else if (filter.type === "date") {
    return <Calendar id={filter.id} showIcon iconPos="left" dateFormat={SEARCH_DATE_FILTER_FORMAT} showButtonBar onChange={(e) => filter.onChangeFn(formatJSDate(e.value))} />;
  } else if (filter.type === "checkbox") {
    return (
      <div className="flex align-content-center flex-wrap justify-content-center mt-3">
        <Checkbox inputId={filter.id} value={filter.checked} checked={filter.checked} onChange={(e) => filter.onChangeFn(e.checked)}></Checkbox>
      </div>
    );
  }
  return null;
}
