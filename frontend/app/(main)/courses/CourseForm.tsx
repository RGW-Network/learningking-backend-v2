'use client';
import { Dialog } from 'primereact/dialog';
import { Messages } from 'primereact/messages';
import { ReactNode, useEffect, useRef, useState } from 'react';
import { Button } from 'primereact/button';
import * as labels from '../../constants/Labels';
import useShowModalDialog from '../../components/ShowModalHook';
import { PrimeIcons } from 'primereact/api';
import { BaseApiServiceImpl } from '../../api/BaseApiServiceImpl';
import { MessageUtils } from '../../utils/MessageUtils';
import { formatString, replaceWithUnderscore, toReadableDate } from '../../utils/Utils';
import { getFilterComponent } from '../../components/Filters';
import { paginatorTemplate } from '../../components/PaginatorTemplate';
import { filtersHeadertemplate } from '../../components/FiltersPanelHeader';
import UserFormDialogView from '../users/UserFormDialogView';
import { getFormFieldComponent, validateEmptyField } from '../../components/FormFieldTemplates';
import { ACADEMIES_ENUM, CATEGORY_TYPES_ENUM, CSS_COL_12, CSS_COL_6, LOOKUP_YPES, MAXIMUM_RECORDS_PER_PAGE, RECORD_STATUSES } from '../../constants/Constants';
import { types } from 'util';
import { FormFieldTypes } from '@/app/constants/FormFieldTypes';
import { MISSING_FORM_INPUT_MESSAGE } from '@/app/constants/ErrorMessages';

interface ModalType {
    children?: ReactNode;
    messageRef?: any;
    record: any;
    lookupTypes: any;
    reloadFn: any;
    isOpen: boolean;
    toggle: () => void;
}

const CategoryFormDialog = (props: ModalType) => {
    const [recordId, setRecordId] = useState<string | null>(null);
    const [name, setName] = useState<string | null>(null);
    const [description, setDescription] = useState<string | null>(null);

    const [type, setType] = useState<any>(null);
    const [academy, setAcademy] = useState<any>(null);
    const [recordStatus, setRecordStatus] = useState<string | null>(null);
    const [lookupTypes, setlookupTypes] = useState<any>([]);
    const [isValidValueHint, setIsValidValueHint] = useState<string | null>(null);
    const [isValidDescriptionHint, setIsValidDescriptionHint] = useState<string | null>(null);
    const [isValidTypeHint, setIsValidTypeHint] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const message = useRef<any>();

    /**
     * This hook is called when the object is changed. This happens
     * in the parent view when the is changed
     */
    useEffect(() => {
        setlookupTypes(props?.lookupTypes);
        populateForm(props?.record);
    }, [props?.record]);

    /**
     * This clears the form by setting form values to null
     */
    const clearForm = () => {
        populateForm(null);
    };

    const populateForm = (dataObject: any) => {
        setRecordId(dataObject?.id);
        setName(dataObject?.name);
        setType(dataObject?.type.id);
        setAcademy(dataObject?.academy.id);
        setDescription(dataObject?.description);
    };

    /**
     * This is a list of user form fields
     */
    let userFormFields: any = [
        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'Name',
            value: name,
            onChange: setName,
            setHint: setIsValidValueHint,
            isValidHint: isValidValueHint,
            validateFieldFn: validateEmptyField,
            width: CSS_COL_6
        },
        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'Description',
            value: description,
            onChange: setDescription,
            width: CSS_COL_6
        },

        {
            type: FormFieldTypes.DROPDOWN.toString(),
            label: 'Type',
            value: type,
            onChange: setType,
            options: CATEGORY_TYPES_ENUM,
            optionValue: 'id',
            optionLabel: 'name',
            width: CSS_COL_6
        },

        {
            type: FormFieldTypes.DROPDOWN.toString(),
            label: 'academy',
            value: academy,
            onChange: setAcademy,
            options: ACADEMIES_ENUM,
            optionValue: 'id',
            optionLabel: 'name',
            width: CSS_COL_6
        }
    ];

    /**
     * This loops through the user object fields array to create the fields elements for
     * display
     */
    let userFields = userFormFields.map((userObjectField: any) => {
        return getFormFieldComponent(userObjectField);
    });

    /**
     * This clears the hint messages
     */
    const clearHints = () => {
        userFormFields.forEach((formField: any) => {
            if (formField.isValidHint) {
                formField.setHint(null);
            }
        });
    };

    /**
     * This validates the form fields that have isValidHint attributes and sets their corresponding hints if the field validation
     * fails
     * @returns boolean
     */
    const validateForm = () => {
        clearHints();
        let isFormValid: boolean = true;

        userFormFields.forEach((formField: any) => {
            if (formField.setHint && (formField.value === null || formField.value === '' || formField.value === undefined)) {
                isFormValid = false;
                formField.setHint(formatString(MISSING_FORM_INPUT_MESSAGE, formField.label));
            }
        });

        return isFormValid;
    };

    /**
     * This submits a save user request to the backoffice
     */
    const saveUser = () => {
        let userData: any = {
            id: recordId,
            name,
            description,
            typeId: type,
            academyId: academy
        };

        if (validateForm()) {
            setIsSaving(true);
            new BaseApiServiceImpl('/v1/categories')
                .postRequestWithJsonResponse(userData)
                .then(async (response) => {
                    setIsSaving(false);
                    clearForm();
                    MessageUtils.showSuccessMessage(props?.messageRef, labels.LABEL_RECORD_SAVED_SUCCESSFULLY);
                    closeDialog();
                    props?.reloadFn();
                })
                .catch((error) => {
                    setIsSaving(false);
                    MessageUtils.showErrorMessage(message, error.message);
                });
        }
    };

    /**
     * This closes the dialog
     */
    const closeDialog = () => {
        props.toggle();
    };

    /**
     * This is the footer of the modal dialog
     */
    const userDetailsDialogFooter = (
        <>
            <Button label={labels.LABEL_CANCEL} icon={PrimeIcons.TIMES} className="p-button-text" onClick={closeDialog} />
            <Button label={labels.LABEL_SAVE} icon={PrimeIcons.SAVE} className="p-button-secondary" onClick={saveUser} loading={isSaving} />
        </>
    );

    return (
        <Dialog visible={props.isOpen} header={'Create category'} footer={userDetailsDialogFooter} modal className="p-fluid" onHide={closeDialog} style={{ width: '50vw' }}>
            <Messages ref={message} />
            <div className="grid">
                <div className="col-12">
                    <Messages ref={message} style={{ width: '100%' }} />
                </div>
                {userFields}
            </div>
        </Dialog>
    );
};

export default CategoryFormDialog;
