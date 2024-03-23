'use client';
import { Button } from 'primereact/button';
import { Chip } from 'primereact/chip';
import { Dropdown } from 'primereact/dropdown';
import { Editor } from 'primereact/editor';
import { FileUpload, FileUploadSelectEvent, FileUploadUploadEvent, ItemTemplateOptions } from 'primereact/fileupload';
import { InputSwitch } from 'primereact/inputswitch';
import { InputText } from 'primereact/inputtext';
import { classNames } from 'primereact/utils';
import React, { useRef, useState } from 'react';
import type { Demo } from '@/types';
import { InputTextarea } from 'primereact/inputtextarea';
import { BaseApiServiceImpl } from '@/app/api/BaseApiServiceImpl';
import { MessageUtils } from '@/app/utils/MessageUtils';

function CourseForm() {
    const [course, setCourse] = useState<any>({
        title: '',
        description: '',
        coverImageUrl: '',
        welcomeVideoUrl: '',
        guidelineVideoUrl: '',
        publicationStatus: 'Draft',
        tags: ['Nike', 'Sneaker'],
        category: 'Sneakers',
        certificateTemplate: [],
        stock: 'Sneakers',
        ownershipType: true,
        isFeatured: true,
        isPaid: true,
        cost: true,
        fullDescription: true,
        academy: '',
        company: []
    });

    const [categories, setCategories] = useState(course.category);
    const [selectedStock, setSelectedStock] = useState(course.category);
    const categoryOptions = ['Sneakers', 'Apparel', 'Socks'];

    const fileUploader = useRef<FileUpload>(null);
    const fetchRecordsFromServer = () => {
        new BaseApiServiceImpl('/v1/categories')
            .getRequestWithJsonResponse({})
            .then(async (response) => {
                setCategories(response?.records);
            })
            .catch((error) => {
                MessageUtils.showErrorMessage('Categories', error.message);
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
        setCourse((prevState: any) => ({ ...prevState, images: event.files }));
    };

    const onFileUploadClick = () => {
        const inputEl = fileUploader.current?.getInput();
        inputEl?.click();
    };

    const emptyTemplate = () => {
        return (
            <div className="h-15rem overflow-y-auto py-3 border-round" style={{ cursor: 'copy' }}>
                <div className="flex flex-column w-full h-full justify-content-center align-items-center" onClick={onFileUploadClick}>
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
        <div className="card">
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
                            <InputTextarea value={course.description} style={{ height: '250px' }}></InputTextarea>
                        </div>
                        <div className="col-12 field">
                            <Editor value={course.fullDescription} style={{ height: '250px' }}></Editor>
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
                            <Dropdown options={categories} value={course.category} 
                           
                            onChange={(e) =>
                                setCourse((prevState: any) => ({
                                    ...prevState,
                                    category: e.value
                                }))
                            }
                            placeholder="Select a category"></Dropdown>
                        </div>
                    </div>

                    <div className="border-1 surface-border border-round">
                        <span className="text-900 font-bold block border-bottom-1 surface-border p-3">Stock</span>
                        <div className="p-3">
                            <Dropdown options={categoryOptions} value={selectedStock} onChange={(e) => setSelectedStock(e.value)} placeholder="Select stock"></Dropdown>
                        </div>
                    </div>

                    <div className="flex flex-column sm:flex-row justify-content-between align-items-center gap-3 py-2">
                        <Button className="flex-1" severity="danger" outlined label="Discard" icon="pi pi-fw pi-trash"></Button>
                        <Button className="flex-1 border-round" label="Save" icon="pi pi-fw pi-check"></Button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default CourseForm;
