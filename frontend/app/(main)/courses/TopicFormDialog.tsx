'use client';
import { Dialog } from 'primereact/dialog';
import { Messages } from 'primereact/messages';
import { ReactNode, useRef, useState } from 'react';
import { Button } from 'primereact/button';
import * as labels from '../../constants/Labels';
import { PrimeIcons } from 'primereact/api';
import { BaseApiServiceImpl } from '../../api/BaseApiServiceImpl';
import { MessageUtils } from '../../utils/MessageUtils';
import { formatString } from '../../utils/Utils';
import { getFormFieldComponent } from '../../components/FormFieldTemplates';
import { CSS_COL_12, CSS_COL_4, CSS_COL_6, CSS_COL_8, PUBLICATION_STATUSES } from '../../constants/Constants';
import { FormFieldTypes } from '@/app/constants/FormFieldTypes';
import { MISSING_FORM_INPUT_MESSAGE } from '@/app/constants/ErrorMessages';

interface ModalType {
    children?: ReactNode;
    messageRef?: any;
    record: any;
    lessons: [];
    reloadFn: any;
    isOpen: boolean;
    toggle: () => void;
}
interface RecordType {
    id: 0;
    title: '';
    description: '';
    position: 1;
    publicationStatus: '';
    courseLessonId: 0;
}

const TopicFormDialog = (props: ModalType) => {
    const [record, setRecord] = useState<RecordType | null>(props.record);
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const message = useRef<any>();

    /**
     * This clears the form by setting form values to null
     */
    const clearForm = () => {
        setRecord(null);
    };

    /**
     * This is a list of user form fields
     */
    let userFormFields: any = [
        {
            type: FormFieldTypes.TEXT.toString(),
            label: 'Title',
            value: record?.title,
            onChange: (e: any) => setRecord((prevState: any) => ({ ...prevState, title: e })),
            width: CSS_COL_12
        },
        {
            type: FormFieldTypes.NUMBER.toString(),
            label: 'Position',
            value: record?.position,
            onChange: (e: any) => setRecord((prevState: any) => ({ ...prevState, position: e })),
            width: CSS_COL_4
        },
        {
            type: FormFieldTypes.DROPDOWN.toString(),
            label: 'Publication Status',
            value: record?.publicationStatus,
            onChange: (e: any) => setRecord((prevState: any) => ({ ...prevState, publicationStatus: e })),
            options: PUBLICATION_STATUSES,
            optionValue: 'id',
            optionLabel: 'name',
            width: CSS_COL_8
        },
        {
            type: FormFieldTypes.DROPDOWN.toString(),
            label: 'Lessons',
            value: record?.courseLessonId,
            onChange: (e: any) => setRecord((prevState: any) => ({ ...prevState, courseLessonId: e })),
            options: props.lessons,
            optionValue: 'id',
            optionLabel: 'title',
            width: CSS_COL_8
        },

        {
            type: FormFieldTypes.TEXTAREA.toString(),
            label: 'Description',
            value: record?.description,
            onChange: (e: any) => setRecord((prevState: any) => ({ ...prevState, description: e })),
            width: CSS_COL_12
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
    const doSave = () => {
        if (validateForm()) {
            setIsSaving(true);
            new BaseApiServiceImpl('/v1/admin/course-topics')
                .postRequestWithJsonResponse(record)
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
    const dialogFooter = (
        <>
            <Button label={labels.LABEL_CANCEL} icon={PrimeIcons.TIMES} className="p-button-text" onClick={closeDialog} />
            <Button label={labels.LABEL_SAVE} icon={PrimeIcons.SAVE} className="p-button-secondary" onClick={doSave} loading={isSaving} />
        </>
    );

    return (
        <Dialog visible={props.isOpen} header={'Course Topic Form'} footer={dialogFooter} modal className="p-fluid" onHide={closeDialog} style={{ width: '70vw' }}>
            <div className="grid">
                <div className="col-12">
                    <Messages ref={message} style={{ width: '100%' }} />
                </div>
                {userFields}
            </div>
        </Dialog>
    );
};

export default TopicFormDialog;
