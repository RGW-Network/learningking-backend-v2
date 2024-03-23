'use client';

import { Dialog } from 'primereact/dialog';
import { Messages } from 'primereact/messages';
import { ReactNode, useEffect, useRef, useState } from 'react';
import { PrimeIcons } from 'primereact/api';
import { Button } from 'primereact/button';
import * as labels from '../../constants/Labels';
import { CSS_COL_12, CSS_COL_6, MAXIMUM_RECORDS_PER_PAGE } from '../../constants/Constants';
import { BaseApiServiceImpl } from '@/app/api/BaseApiServiceImpl';
import { MessageUtils } from '@/app/utils/MessageUtils';
import { FormFieldTypes } from '@/app/constants/FormFieldTypes';
import { getFormFieldComponent, validateEmptyField } from '@/app/components/FormFieldTemplates';
import { formatString } from '@/app/utils/Utils';
import { MISSING_FORM_INPUT_MESSAGE } from '@/app/constants/ErrorMessages';

interface ModalType {
    children?: ReactNode;
    messageRef?: any;
    record: any;
    reloadFn: any;
    isOpen: boolean;
    toggle: () => void;
}

const UserFormDialogView = (props: ModalType) => {
    const [recordId, setRecordId] = useState<string | null>(null);
    const [firstName, setFirstName] = useState<string | null>(null);
    const [lastName, setLastName] = useState<string | null>(null);
    const [userName, setUserName] = useState<string | null>(null);
    const [gender, setGender] = useState<string | null>(null);
    const [genders, setGenders] = useState<string | null>(null);

    const [roleIds, setRoleIds] = useState<any>([]);
    const [roles, setRoles] = useState<any>([]);
    const [isValidFirstNameHint, setIsValidFirstNameHint] = useState<string | null>(null);

    const [isValidLastNameHint, setIsValidLastNameHint] = useState<string | null>(null);
    const [isValidUsernameHint, setIsValidUsernameHint] = useState<string | null>(null);
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const message = useRef<any>();

    /**
     * This hook is called when the object is changed. This happens
     * in the parent view when the is changed
     */
    useEffect(() => {
        populateForm(props?.record);
        fetchGendersFromServer();
        fetchRolesFromServer();
    }, [props?.record]);

    const fetchGendersFromServer = () => {
        new BaseApiServiceImpl('/v1/lookups/genders')
            .getRequestWithJsonResponse({})
            .then(async (response) => {
                setGenders(response?.records);
            })
            .catch((error) => {
                MessageUtils.showErrorMessage(message, error.message);
            });
    };

    const fetchRolesFromServer = () => {
        new BaseApiServiceImpl('/v1/users/roles')
            .getRequestWithJsonResponse({ offset: 0, limit: MAXIMUM_RECORDS_PER_PAGE })
            .then(async (response) => {
                setRoles(response?.records);
            })
            .catch((error) => {
                MessageUtils.showErrorMessage(message, error.message);
            });
    };

    /**
     * This clears the form by setting form values to null
     */
    const clearForm = () => {
        populateForm(null);
    };

    const populateForm = (dataObject: any) => {
        setRecordId(dataObject?.id);
        setFirstName(dataObject?.firstName);
        setLastName(dataObject?.lastName);
        setUserName(dataObject?.userName);
        setRoleIds(dataObject?.roleIds);
    };

    /**
     * This is a list of user form fields
     */
    let userFormFields: any = [
        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'First Name',
            value: firstName,
            onChange: setFirstName,
            setHint: setIsValidFirstNameHint,
            isValidHint: isValidFirstNameHint,
            validateFieldFn: validateEmptyField,
            width: CSS_COL_6
        },
        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'Last Name',
            value: lastName,
            onChange: setLastName,
            setHint: setIsValidLastNameHint,
            isValidHint: isValidLastNameHint,
            validateFieldFn: validateEmptyField,
            width: CSS_COL_6
        },

        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'Email Address',
            value: userName,
            onChange: setUserName,
            setHint: setIsValidUsernameHint,
            isValidHint: isValidUsernameHint,
            validateFieldFn: validateEmptyField,
            width: CSS_COL_6
        },

        {
            type: FormFieldTypes.DROPDOWN.toString(),
            label: 'Gender',
            value: gender,
            onChange: setGender,
            options: genders,
            optionValue: 'id',
            optionLabel: 'name',
            width: CSS_COL_6
        },

        {
            type: FormFieldTypes.MULTISELECT.toString(),
            label: 'Roles',
            value: roleIds,
            onChange: setRoleIds,
            options: roles,
            optionValue: 'id',
            optionLabel: 'name'
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
            firstName,
            lastName,
            username: userName,
            emailAddress: userName,
            initialPassword: userName,
            genderId: gender,
            roleIds: roleIds
        };

        if (validateForm()) {
            setIsSaving(true);
            new BaseApiServiceImpl('/api/v1/users')
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
        <Dialog visible={props.isOpen} header={'Create user form'} footer={userDetailsDialogFooter} modal className="p-fluid" onHide={closeDialog} style={{ width: '50vw' }}>
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

export default UserFormDialogView;
