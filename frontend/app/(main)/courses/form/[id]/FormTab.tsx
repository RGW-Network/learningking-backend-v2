'use client';
import { Button } from 'primereact/button';
import { Chip } from 'primereact/chip';
import { Dropdown } from 'primereact/dropdown';
import { Editor } from 'primereact/editor';
import { FileUpload, FileUploadSelectEvent, FileUploadUploadEvent, ItemTemplateOptions } from 'primereact/fileupload';
import { InputSwitch } from 'primereact/inputswitch';
import { InputText } from 'primereact/inputtext';
import { classNames } from 'primereact/utils';
import React, { useEffect, useRef, useState } from 'react';
import type { Demo } from '@/types';
import { InputTextarea } from 'primereact/inputtextarea';
import { BaseApiServiceImpl } from '@/app/api/BaseApiServiceImpl';
import { MessageUtils } from '@/app/utils/MessageUtils';
import { LABEL_RECORD_SAVED_SUCCESSFULLY } from '@/app/constants/Labels';
import { ACADEMIES_ENUM, COURSE_OWNERSHIPS_ENUM } from '@/app/constants/Constants';
import { formatJSDate, isNotEmpty, jsonToFormData } from '@/app/utils/Utils';
import { useParams, usePathname, useRouter, useSearchParams } from 'next/navigation';

function FormTab() {
    const pathParam = useParams();

    const recordId = pathParam.id;
    console.log('Current Record Id ' + recordId);
    const [course, setCourse] = useState<any>({
        id: recordId,
        title: '',
        description: '',
        coverImageUrl: '',
        welcomeVideoUrl: '',
        guidelineVideoUrl: '',
        publicationStatus: 0,
        tags: [],
        categoryId: 0,
        certificateTemplate: '',
        ownershipTypeId: 0,
        isFeatured: true,
        isPaid: true,
        cost: 0,
        fullDescription: '',
        academyId: 0,
        companyId: 0,
        coverImage: null
    });
    const [isSaving, setIsSaving] = useState<boolean>(false);
    const router = useRouter();
    const [profilePhoto, setProfilePhoto] = useState<any>(null);

    const [categories, setCategories] = useState([]);

    const fileUploader = useRef<FileUpload>(null);
    const fetchCategoriesFromServer = () => {
        new BaseApiServiceImpl('/v1/categories')
            .getRequestWithJsonResponse({ offset: 0, limit: 100 })
            .then(async (response) => {
                setCategories(response?.records);
            })
            .catch((error) => {
                MessageUtils.showErrorMessage('Categories', error.message);
            });
    };

    const fetchRecordFromServer = () => {
        new BaseApiServiceImpl('/v1/admin/courses/' + recordId)
            .getRequestWithJsonResponse({})
            .then(async (response) => {
                setCourse(response?.data);
            })
            .catch((error) => {
                MessageUtils.showErrorMessage('Categories', error.message);
            });
    };
    /**
     * This hook is called everytime the page is loaded
     */
    useEffect(() => {
        if (isNotEmpty(recordId) && Number(recordId) > 0) {
            fetchRecordFromServer();
        }

        fetchCategoriesFromServer();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);
    const clearForm = () => {};
    /**
     * This submits a save user request to the backoffice
     */
    const doSave = () => {
        let userData: any = course;

        let accountProfileFormData: FormData = new FormData();
        accountProfileFormData.append('file', course.coverImage);
        userData.coverImage = null;
        accountProfileFormData.append(
            'dto',
            new Blob([JSON.stringify(userData)], {
                type: 'application/json'
            })
        );

        setIsSaving(true);
        new BaseApiServiceImpl('/v1/admin/courses')
            .postMultipartWithJsonResponse(accountProfileFormData)
            .then(async (response) => {
                setIsSaving(false);
                clearForm();
                MessageUtils.showSuccessMessage('', LABEL_RECORD_SAVED_SUCCESSFULLY);
            })
            .catch((error) => {
                setIsSaving(false);
                MessageUtils.showErrorMessage('', error.message);
            });
    };

    const chipTemplate = (tag: string) => {
        return (
            <React.Fragment>
                <span className="mr-3">{tag}</span>
                <span className="chip-remove-icon flex align-items-center justify-content-center border-1 surface-border bg-gray-100 border-circle cursor-pointer" onClick={() => onChipRemove(tag)}>
                    <i className="pi pi-fw pi-times text-black-alpha-60"></i>
                </span>
            </React.Fragment>
        );
    };

    const onImageMouseOver = (ref: React.RefObject<Button>, fileName: string) => {
        if ((ref.current as any).id === fileName) (ref.current as any).style.display = 'flex';
    };

    const onImageMouseLeave = (ref: React.RefObject<Button>, fileName: string) => {
        if ((ref.current as any).id === fileName) {
            (ref.current as any).style.display = 'none';
        }
    };

    const onChipRemove = (item: string) => {
        const newTags = (course.tags as string[])?.filter((i) => i !== item);
        setCourse((prevState: any) => ({ ...prevState, tags: newTags }));
    };

    const onUpload = (event: FileUploadUploadEvent | FileUploadSelectEvent) => {
        setCourse((prevState: any) => ({ ...prevState, coverImage: event.files[0] }));
    };

    const onFileUploadClick = () => {
        const inputEl = fileUploader.current?.getInput();
        inputEl?.click();
    };
    /**
     * This uploads the photo and sets the variable profilePhoto. It clears the file
     * form once completed
     * @param event
     */
    const uploadPhoto = (event: any) => {
        setProfilePhoto(event.files[0]);
        event.options.clear();
    };
    const emptyTemplate = () => {
        return (
            <div className="h-15rem overflow-y-auto py-3 border-round" style={{ cursor: 'copy' }}>
                <div className="flex flex-column w-full h-full justify-content-center align-items-center" onClick={onFileUploadClick}>
                    {isNotEmpty(course.coverImageUrl) && <img src={course.coverImageUrl} className="w-full h-full border-round shadow-2" alt={'Cover Image'} />}
                    <i className="pi pi-file text-4xl text-primary"></i>

                    <span className="block font-semibold text-900 text-lg mt-3">Drop or select images</span>
                </div>
            </div>
        );
    };

    const itemTemplate = (file: object, props: ItemTemplateOptions) => {
        const item = file as Demo.Base;
        const buttonEl = React.createRef<Button>();
        return (
            <div className="flex h-15rem overflow-y-auto py-3 border-round" style={{ cursor: 'copy' }} onClick={onFileUploadClick}>
                <div className="flex flex-row flex-wrap gap-3 border-round">
                    <div
                        className="h-full relative w-7rem h-7rem border-3 border-transparent border-round hover:bg-primary transition-duration-100 cursor-auto"
                        onMouseEnter={() => onImageMouseOver(buttonEl, item.name)}
                        onMouseLeave={() => onImageMouseLeave(buttonEl, item.name)}
                        style={{ padding: '1px' }}
                    >
                        <img src={item.objectURL} className="w-full h-full border-round shadow-2" alt={item.name} />
                        <Button
                            ref={buttonEl}
                            id={item.name}
                            type="button"
                            icon="pi pi-times"
                            className="hover:flex text-sm absolute justify-content-center align-items-center cursor-pointer w-2rem h-2rem"
                            rounded
                            style={{ top: '-10px', right: '-10px', display: 'none' }}
                            onClick={(event) => {
                                event.stopPropagation();
                                props.onRemove(event);
                            }}
                        ></Button>
                    </div>
                </div>
            </div>
        );
    };

    return (
        <div className="">
            <span className="block text-900 font-bold text-xl mb-4">Create Course</span>
            <div className="grid grid-nogutter flex-wrap gap-3 p-fluid">
                <div className="col-12 lg:col-8">
                    <div className="grid formgrid">
                        <div className="col-12 field">
                            <InputText
                                type="text"
                                value={course.title}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        title: e.target.value
                                    }))
                                }
                                placeholder="Course title"
                            />
                        </div>
                        <div className="col-12 lg:col-4 field">
                            <InputText
                                type="text"
                                placeholder="Price"
                                value={course.cost?.toString()}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        cost: parseFloat(e.target.value) || undefined
                                    }))
                                }
                            />
                        </div>
                        <div className="col-12 lg:col-4 field">
                            <InputText
                                type="text"
                                placeholder="welcomeVideoUrl"
                                value={course.welcomeVideoUrl}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        welcomeVideoUrl: e.target.value
                                    }))
                                }
                            />
                        </div>
                        <div className="col-12 lg:col-4 field">
                            <InputText
                                type="text"
                                placeholder="guidelineVideoUrl"
                                value={course.guidelineVideoUrl as string}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        guidelineVideoUrl: e.target.value
                                    }))
                                }
                            />
                        </div>
                        <div className="col-12 field">
                            <InputTextarea
                                value={course.description}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        description: e.target.value
                                    }))
                                }
                                style={{ height: '250px' }}
                            ></InputTextarea>
                        </div>
                        <div className="col-12 field">
                            <Editor
                                value={course.fullDescription}
                                onTextChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        fullDescription: e.textValue
                                    }))
                                }
                                style={{ height: '250px' }}
                            ></Editor>
                        </div>
                        <div className="col-12 field">
                            <FileUpload
                                ref={fileUploader}
                                name="demo[]"
                                url="./upload.php"
                                itemTemplate={itemTemplate}
                                emptyTemplate={emptyTemplate}
                                onUpload={onUpload}
                                customUpload={true}
                                multiple={false}
                                onSelect={onUpload}
                                accept="image/*"
                                auto
                                className={'upload-button-hidden border-1 surface-border surface-card border-round'}
                            />
                        </div>
                    </div>
                </div>

                <div className="flex-1 w-full lg:w-3 xl:w-4 flex flex-column row-gap-3">
                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Publish</span>
                        <div className="p-3">
                            <div className="bg-gray-100 py-2 px-3 flex align-items-center border-round">
                                <span className="text-black-alpha-90 font-bold mr-3">Status:</span>
                                <span className="text-black-alpha-60 font-semibold">{course.status as string}</span>
                                <Button type="button" icon="pi pi-fw pi-pencil" rounded text className="text-black-alpha-60 ml-auto"></Button>
                            </div>
                        </div>
                    </div>

                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Tags</span>
                        <div className="p-3 flex flex-wrap gap-1">
                            {(course.tags as string[])?.map((tag, i) => {
                                return <Chip key={i} className="mr-2 py-2 px-3 text-900 font-bold surface-card border-1 surface-border" style={{ borderRadius: '20px' }} template={() => chipTemplate(tag)} />;
                            })}
                        </div>
                    </div>

                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Category</span>
                        <div className="p-3">
                            <Dropdown
                                optionLabel="name"
                                optionValue="id"
                                filter
                                showClear
                                options={categories}
                                value={course.categoryId}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        categoryId: e.value
                                    }))
                                }
                                placeholder="Select a category"
                            ></Dropdown>
                        </div>
                    </div>
                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Ownership</span>
                        <div className="p-3">
                            <Dropdown
                                optionLabel="name"
                                optionValue="id"
                                filter
                                showClear
                                options={COURSE_OWNERSHIPS_ENUM}
                                value={course.ownershipTypeId}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        ownershipTypeId: e.value
                                    }))
                                }
                                placeholder="Select a owner"
                            ></Dropdown>
                        </div>
                    </div>
                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Academy</span>
                        <div className="p-3">
                            <Dropdown
                                optionLabel="name"
                                optionValue="id"
                                filter
                                showClear
                                options={ACADEMIES_ENUM}
                                value={course.academyId}
                                onChange={(e) =>
                                    setCourse((prevState: any) => ({
                                        ...prevState,
                                        academyId: e.value
                                    }))
                                }
                                placeholder="Select stock"
                            ></Dropdown>
                        </div>
                    </div>

                    <div className="flex flex-column sm:flex-row justify-content-between align-items-center gap-3 py-2">
                        <Button loading={isSaving} className="flex-1" severity="danger" outlined label="Discard" icon="pi pi-fw pi-trash"></Button>
                        <Button loading={isSaving} className="flex-1 border-round" label="Save" onClick={doSave} icon="pi pi-fw pi-check"></Button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default FormTab;
