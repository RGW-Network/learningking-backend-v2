'use client';
import React, { ReactNode, useRef, useState } from 'react';
import { Toast } from 'primereact/toast';
import { FileUpload, FileUploadHeaderTemplateOptions, ItemTemplateOptions } from 'primereact/fileupload';
import { ProgressBar } from 'primereact/progressbar';
import { Button } from 'primereact/button';
import { Tooltip } from 'primereact/tooltip';
import { Tag } from 'primereact/tag';
import { isNotEmpty } from '../utils/Utils';

interface ModalType {
    children?: ReactNode;
    messageRef?: any;
    value: any;
    setValue: (e: any) => void;
}
export default function CustomImagePicker(mainProps: ModalType) {
    const toast = useRef<Toast>(null);
    const [totalSize, setTotalSize] = useState(0);
    const [image, setImage] = useState(0);
    const fileUploadRef = useRef<FileUpload>(null);

    const onTemplateSelect = (e: any) => {
        let _totalSize = totalSize;
        let files = e.files;

        for (let i = 0; i < files.length; i++) {
            _totalSize += files[i].size || 0;
        }

        setTotalSize(_totalSize);
    };
    /**
     * This uploads the photo and sets the variable profilePhoto. It clears the file
     * form once completed
     * @param event
     */
    const uploadPhoto = (event: any) => {
        setImage(event.files[0]);
        mainProps.setValue(event.files[0]);
        // event.options.clear();
    };
    const onTemplateUpload = (e: any) => {
        let _totalSize = 0;

        e.files.forEach((file: any) => {
            _totalSize += file.size || 0;
        });

        setTotalSize(_totalSize);
        toast.current?.show({
            severity: 'info',
            summary: 'Success',
            detail: 'File Uploaded'
        });
    };

    const onTemplateRemove = (file: File, callback: Function) => {
        setTotalSize(totalSize - file.size);
        callback();
    };

    const onTemplateClear = () => {
        setTotalSize(0);
    };

    const headerTemplate = (options: FileUploadHeaderTemplateOptions) => {
        const { className, chooseButton, uploadButton, cancelButton } = options;
        const value = totalSize / 10000;

        return (
            <div
                className={className}
                style={{
                    backgroundColor: 'transparent',
                    display: 'flex',
                    alignItems: 'center'
                }}
            >
                {chooseButton}

                {cancelButton}
            </div>
        );
    };

    const itemTemplate = (inFile: object, props: ItemTemplateOptions) => {
        const file: any = inFile as File;
        return (
            <div className="flex align-items-center flex-wrap">
                <div className="flex align-items-center" style={{ width: '70%' }}>
                    <img alt={file.name} role="presentation" src={file.objectURL} width={100} />
                    {/* <span className="flex flex-column text-left ml-3">
            {file.name}
            <small>{new Date().toLocaleDateString()}</small>
          </span> */}
                </div>
                {/* <Tag
          value={props.formatSize}
          severity="warning"
          className="px-3 py-2"
        />
        <Button
          type="button"
          icon="pi pi-times"
          className="p-button-outlined p-button-rounded p-button-danger ml-auto"
          onClick={() => onTemplateRemove(file, props.onRemove)}
        /> */}
            </div>
        );
    };

    const emptyTemplate = () => {
        return isNotEmpty(mainProps.value) ? (
            <div className="flex align-items-center flex-column">
                <img alt={'Image'} role="presentation" src={mainProps.value} width={100} />
            </div>
        ) : (
            <div className="flex align-items-center flex-column">
                <i
                    className="pi pi-image mt-3 p-5"
                    style={{
                        fontSize: '5em',
                        borderRadius: '50%',
                        backgroundColor: 'var(--surface-b)',
                        color: 'var(--surface-d)'
                    }}
                ></i>

                <span style={{ fontSize: '1.2em', color: 'var(--text-color-secondary)' }} className="my-5">
                    Drag and Drop Image Here
                </span>
            </div>
        );
    };

    const chooseOptions = {
        icon: 'pi pi-fw pi-images',
        iconOnly: true,
        className: 'custom-choose-btn p-button-rounded p-button-outlined'
    };
    const cancelOptions = {
        icon: 'pi pi-fw pi-times',
        iconOnly: true,
        className: 'custom-cancel-btn p-button-danger p-button-rounded p-button-outlined'
    };

    return (
        <div>
            <Toast ref={toast}></Toast>

            <Tooltip target=".custom-choose-btn" content="Choose" position="bottom" />
            <Tooltip target=".custom-cancel-btn" content="Clear" position="bottom" />

            <FileUpload
                ref={fileUploadRef}
                name="demo[]"
                multiple={false}
                accept="image/*"
                maxFileSize={1000000}
                onUpload={onTemplateUpload}
                onSelect={onTemplateSelect}
                onError={onTemplateClear}
                onClear={onTemplateClear}
                headerTemplate={headerTemplate}
                itemTemplate={itemTemplate}
                emptyTemplate={emptyTemplate}
                chooseOptions={chooseOptions}
                cancelOptions={cancelOptions}
                customUpload
                auto
                uploadHandler={uploadPhoto}
            />
        </div>
    );
}
