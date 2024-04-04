import { Calendar } from 'primereact/calendar';
import { Dropdown } from 'primereact/dropdown';
import { InputNumber } from 'primereact/inputnumber';
import { InputText } from 'primereact/inputtext';
import { FormFieldTypes } from '../constants/FormFieldTypes';
import { formatString, getDefaultYearRange } from '../utils/Utils';
import { MISSING_FORM_INPUT_MESSAGE } from '../constants/ErrorMessages';
import { InputTextarea } from 'primereact/inputtextarea';
import { MultiSelect } from 'primereact/multiselect';
import { TriStateCheckbox } from 'primereact/tristatecheckbox';
import { Chips } from 'primereact/chips';
import { FileUpload } from 'primereact/fileupload';
import CustomImagePicker from './CustomImagePicker';
import { Editor } from 'primereact/editor';

/**
 * This validates a field by checking if the value is null or empty
 * @param value
 * @param setHint
 * @param label
 */
export function validateEmptyField(value: any, setHint: any, label: string) {
    setHint(null);
    if (value === null || value === '' || value === undefined) {
        setHint(formatString(MISSING_FORM_INPUT_MESSAGE, label));
    }
}

/**
 * This returns a form component for display. A form field should have the following attributes:
 * * type
 * * id
 * * label
 * * value
 * * width
 * * onChange
 * * setHint
 * * isValidHint
 * * options (required if type is multiselect or dropdown)
 * * optionValue (required if type is multiselect or dropdown)
 * * optionLabel (required if type is multiselect or dropdown)
 * * validateFieldFn (optional form field validation)
 * * minValue (optional if the type is number)
 * @param formField
 */
export function getFormFieldComponent(formField: any) {
    formField.id = formField.label;
    if (formField?.type === FormFieldTypes.TEXT.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <InputText
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || ''}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                                formField?.validateFieldFn(e.target.value, formField?.setHint, formField?.label);
                            }}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <InputText
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || ''}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                            }}
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.DROPDOWN.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <Dropdown
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            onFilter={formField?.onFilter}
                            options={formField?.options}
                            optionLabel={formField?.optionLabel}
                            optionValue={formField?.optionValue}
                            itemTemplate={formField?.itemTemplate}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                                formField?.validateFieldFn(e.target.value, formField?.setHint, formField?.label);
                            }}
                            placeholder="Select"
                            filter
                            showClear
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <Dropdown
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            options={formField?.options}
                            onFilter={formField?.onFilter}
                            optionLabel={formField?.optionLabel}
                            optionValue={formField?.optionValue}
                            itemTemplate={formField?.itemTemplate}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                            }}
                            placeholder="Select"
                            filter
                            showClear
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.MULTISELECT.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <MultiSelect
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            onFilter={formField?.onFilter}
                            options={formField?.options}
                            optionLabel={formField?.optionLabel}
                            optionValue={formField?.optionValue}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                                formField?.validateFieldFn(e.target.value, formField?.setHint, formField?.label);
                            }}
                            placeholder="Select"
                            maxSelectedLabels={2}
                            display="chip"
                            filter
                            showClear
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <MultiSelect
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            options={formField?.options}
                            onFilter={formField?.onFilter}
                            optionLabel={formField?.optionLabel}
                            optionValue={formField?.optionValue}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                            }}
                            placeholder="Select"
                            maxSelectedLabels={2}
                            display="chip"
                            filter
                            showClear
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.DATE.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <Calendar
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                                formField?.validateFieldFn(e.value, formField?.setHint, formField?.label);
                            }}
                            showIcon
                            showButtonBar
                            yearNavigator={true}
                            monthNavigator={true}
                            yearRange={getDefaultYearRange()}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <Calendar
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                            }}
                            showIcon
                            showButtonBar
                            yearNavigator={true}
                            monthNavigator={true}
                            yearRange={formField?.yearRange !== null && formField?.yearRange !== undefined ? formField?.yearRange : getDefaultYearRange()}
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.NUMBER.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <InputNumber
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || null}
                            disabled={formField?.disabled || false}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                                formField?.validateFieldFn(e.value, formField?.setHint, formField?.label);
                            }}
                            min={formField?.minValue || null}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <InputNumber
                            id={formField?.id}
                            className="w-full"
                            disabled={formField?.disabled || false}
                            value={formField?.value || null}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                            }}
                            min={formField?.minValue || null}
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.FLOAT.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <InputNumber
                            mode="decimal"
                            maxFractionDigits={20}
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || null}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                                formField?.validateFieldFn(e.value, formField?.setHint, formField?.label);
                            }}
                            min={formField?.minValue || null}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <InputNumber
                            mode="decimal"
                            maxFractionDigits={20}
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || null}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                            }}
                            min={formField?.minValue || null}
                        />
                    </>
                )}
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.TEXTAREA.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <InputTextarea
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || ''}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                                formField?.validateFieldFn(e.target.value, formField?.setHint, formField?.label);
                            }}
                            rows={5}
                            autoResize
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <InputTextarea
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value || ''}
                            onChange={(e) => {
                                formField?.onChange(e.target.value);
                            }}
                            rows={5}
                            autoResize
                        />
                    </>
                )}
            </div>
        );
    }
    if (formField?.type === FormFieldTypes.EDITOR.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <Editor
                            value={formField?.value || ''}
                            onTextChange={(e) => {
                                formField?.onChange(e.textValue);
                                formField?.validateFieldFn(e.textValue, formField?.setHint, formField?.label);
                            }}
                            style={{ height: '250px' }}
                        ></Editor>
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <Editor
                            value={formField?.value || ''}
                            onTextChange={(e) => {
                                formField?.onChange(e.textValue);
                            }}
                            style={{ height: '250px' }}
                        ></Editor>
                    </>
                )}
            </div>
        );
    }
    if (formField?.type === FormFieldTypes.BOOLEAN.toString()) {
        return (
            <div key={formField?.id} className={`field-checkbox col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <div className="grid">
                    <div className="col-12">
                        <label htmlFor={formField?.id} className="font-bold">
                            {formField?.label}
                        </label>
                    </div>
                    {formField?.isValidHint && (
                        <div className="col-12">
                            <div className="grid">
                                <div className="col-2">
                                    <TriStateCheckbox
                                        id={formField?.id}
                                        className="w-full"
                                        disabled={formField?.disabled || false}
                                        value={formField?.value}
                                        onChange={(e) => {
                                            formField?.onChange(e.value);
                                            formField?.validateFieldFn(e.target.value, formField?.setHint, formField?.label);
                                        }}
                                    />
                                </div>
                                <div className="col-10">
                                    <label>{formField?.value === undefined ? String(null) : String(formField?.value)}</label>
                                </div>
                                <div className="col-12">
                                    <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                                </div>
                            </div>
                        </div>
                    )}
                    {!formField?.isValidHint && (
                        <div className="col-12">
                            <div className="grid">
                                <div className="col-2">
                                    <TriStateCheckbox
                                        id={formField?.id}
                                        className="w-full"
                                        disabled={formField?.disabled || false}
                                        value={formField?.value}
                                        onChange={(e) => {
                                            formField?.onChange(e.value);
                                        }}
                                    />
                                </div>
                                <div className="col-10">
                                    <label>{formField?.value === undefined ? String(null) : String(formField?.value)}</label>
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        );
    }

    if (formField?.type === FormFieldTypes.CHIPSTEXT.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <Chips
                            id={formField?.id}
                            className="w-full"
                            value={formField?.value}
                            onChange={(e) => {
                                formField?.onChange(e.value);
                                formField?.validateFieldFn(e.value, formField?.setHint, formField?.label);
                            }}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <Chips id={formField?.id} className="w-full" value={formField?.value} onChange={(e) => formField?.onChange(e.value)} />
                    </>
                )}
            </div>
        );
    }
    if (formField?.type === FormFieldTypes.IMAGE.toString()) {
        return (
            <div key={formField?.id} className={`field col-12 md:${formField?.width !== null || formField?.width !== undefined ? formField?.width : 'col-12'}`}>
                <label htmlFor={formField?.id} className="font-bold">
                    {formField?.label}
                </label>
                {formField?.isValidHint && (
                    <>
                        <CustomImagePicker
                            value={formField?.value}
                            setValue={(e) => {
                                formField?.onChange(e);
                                formField?.validateFieldFn(e.value, formField?.setHint, formField?.label);
                            }}
                        />
                        <small className="p-invalid p-error block">{formField?.isValidHint}</small>
                    </>
                )}
                {!formField?.isValidHint && (
                    <>
                        <CustomImagePicker
                            value={formField?.value}
                            setValue={(e) => {
                                formField?.onChange(e);
                            }}
                        />
                    </>
                )}
            </div>
        );
    }
}
