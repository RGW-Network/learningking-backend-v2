'use client';
import { Button } from 'primereact/button';
import { Column } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import React, { useState, useEffect, useRef } from 'react';
import { Messages } from 'primereact/messages';
import { Paginator } from 'primereact/paginator';
import { Panel } from 'primereact/panel';
import * as constants from '../../../../constants/Constants';

import * as labels from '../../../../constants/Labels';
import useShowModalDialog from '../../../../components/ShowModalHook';
import { PrimeIcons } from 'primereact/api';
import { BaseApiServiceImpl } from '../../../../api/BaseApiServiceImpl';
import { MessageUtils } from '../../../../utils/MessageUtils';
import { replaceWithUnderscore, toReadableDate } from '../../../../utils/Utils';
import { getFilterComponent } from '../../../../components/Filters';
import { paginatorTemplate } from '../../../../components/PaginatorTemplate';
import { filtersHeadertemplate } from '../../../../components/FiltersPanelHeader';
import { useParams, useRouter } from 'next/navigation';
import LessonFormDialog from '../../LessonFormDialog';

const LecturesTab = () => {
    const pathParam = useParams();

    const courseId = pathParam.id;
    const [records, setRecords] = useState<any>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [searchTermFilter, setSearchTermFilter] = useState<string | null>(null);
    const [recordStatusFilter, setRecordStatusFilter] = useState<string | null>(null);
    const [totalItems, setTotalItems] = useState<number>(0);
    const [first, setFirst] = useState<number>(0);
    const [limit, setLimit] = useState<number>(constants.MAXIMUM_RECORDS_PER_PAGE);
    const [selectedUser, setSelectedUser] = useState<any>(null);
    const [lookupTypeFilter, setlookupTypeFilter] = useState<any>(null);
    const [lookupTypes, setlookupTypes] = useState<any>([]);
    const router = useRouter();
    let offset = 0;

    const message = useRef<any>();
    const { openDialog, toggleOpenDialog } = useShowModalDialog();
    /**
     * This gets the parameters to submit in the GET request to back office
     * @returns
     */
    const getQueryParameters = () => {
        let searchParameters: any = { offset: offset, limit: limit };
        if (searchTermFilter !== null) searchParameters.searchTerm = searchTermFilter;
        if (recordStatusFilter !== null) searchParameters.recordStatus = recordStatusFilter;
        if (lookupTypeFilter !== null) searchParameters.lookupTypeId = lookupTypeFilter;

        return searchParameters;
    };

    /**
     * This fetches counties from the back office using the search parameters
     */
    const fetchRecordsFromServer = () => {
        setIsLoading(true);
        let searchParameters: any = getQueryParameters();

        new BaseApiServiceImpl('/v1/admin/course-topics')
            .getRequestWithJsonResponse(searchParameters)
            .then(async (response) => {
                setIsLoading(false);
                setRecords(response?.records);
                setTotalItems(response?.totalItems);
            })
            .catch((error) => {
                setIsLoading(false);
                MessageUtils.showErrorMessage(message, error.message);
            });
    };

    /**
     * This hook is called everytime the page is loaded
     */
    useEffect(() => {
        fetchRecordsFromServer();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    /**
     * This is used everytime the user presses the enter button on any form input of a filter
     */
    const onSubmitFilter = () => {
        setSearchTermFilter(searchTermFilter);
        setRecordStatusFilter(recordStatusFilter);
        setlookupTypeFilter(lookupTypeFilter);
        fetchRecordsFromServer();
    };

    /**
     * This is used to clear all filters and resubmit the get request to the back office
     */
    const resetFilters = () => {
        setSearchTermFilter('');
        fetchRecordsFromServer();
    };

    /**
     * This opens the edit territory dialog form by toggling the open dialog variable
     */
    const openEditFormDialog = (selectedRecord: any) => {
        setSelectedUser(selectedRecord);
        toggleOpenDialog();
    };

    /**
     * This opens the new territory dialog form by toggling the open dialog variable
     * and setting the selected territory to null
     */
    const openNewFormDialog = () => {
        toggleOpenDialog();
    };
    /**
     * The row index template
     * @param rowData
     * @param props
     * @returns
     */
    const rowIndexTemplate = (rowData: any, props: any) => {
        return (
            <React.Fragment>
                <span>{first + (props.rowIndex + 1)}</span>
            </React.Fragment>
        );
    };

    /**
     * This is called when a paginator link is clicked
     * @param e
     */
    const onPageChange = (e: any) => {
        offset = e.page * constants.MAXIMUM_RECORDS_PER_PAGE;
        setFirst(e.first);
        setLimit(constants.MAXIMUM_RECORDS_PER_PAGE);

        fetchRecordsFromServer();
    };

    /**
     * The action body template
     * @param rowData
     * @returns
     */
    const actionBodyTemplate = (rowData: any) => {
        return (
            <div className="actions">
                <Button
                    icon={PrimeIcons.PENCIL}
                    className="p-button-sm p-button-warning p-mr-2"
                    onClick={() => {
                        openEditFormDialog(rowData);
                    }}
                />
            </div>
        );
    };

    /**
     * The record status row template
     * @param rowData
     * @returns
     */
    const statusBodyTemplate = (rowData: any) => {
        return <span className={`status-badge status-${rowData?.publicationStatusId?.toLowerCase()}`}>{rowData?.recordStatus}</span>;
    };

    /**
     * The date  row template
     * @param rowData
     * @returns
     */
    const dateTemplate = (rowData: any) => {
        return <>{toReadableDate(rowData.chairpersonDateElected)}</>;
    };

    /**
     * The template for the filter buttons
     */
    const filterButtonsTemplate = (
        <>
            <div className="col-6  md:col-2 p-fluid" key="filterBtns">
                <Button icon={constants.ICON_SEARCH} className={constants.CSS_FILTER_SUBMIT_BUTTON} onClick={onSubmitFilter} loading={isLoading} />
                <Button icon={constants.ICON_REFRESH} className={constants.CSS_FILTER_RESET_BUTTON} onClick={resetFilters} loading={isLoading} />
            </div>
        </>
    );

    /**
     * This is a list of filters to display in the filter section
     */
    const filterDetails = [
        {
            type: 'text',
            value: searchTermFilter,
            onChangeFn: setSearchTermFilter,
            id: 'searchTermFilter',
            label: labels.LABEL_SEARCH_TERM,
            colWidth: constants.CSS_FILTER_SEARCH_INPUT_DIV,
            onkeydownFn: onSubmitFilter
        }
    ];

    /**
     * This loops through the filter Details array and returns the components
     * to render in the detailed form filter dialog.
     */
    const dynamicFilterDetails = filterDetails.map((filter: any) => {
        let component = getFilterComponent(filter);
        return (
            <div className={filter.colWidth} key={replaceWithUnderscore(filter.id)}>
                <label htmlFor={filter.id}>{filter.label}</label>
                {component}
            </div>
        );
    });

    return (
        <div className="grid">
            <div className="col-11 ">
                <div className="grid">
                    {dynamicFilterDetails}
                    {filterButtonsTemplate}
                </div>
            </div>
            <div className="col-1 ">
                <Button label={'Add'} icon={PrimeIcons.PLUS} className="p-button-secondary" onClick={openNewFormDialog} />
            </div>
            <Messages ref={message} style={{ width: '100%' }} />

            <div className="col-12">
                <div className="card">
                    <DataTable value={records} paginator={false} className="datatable-responsive" paginatorPosition="both" emptyMessage="No record found." loading={isLoading}>
                        <Column field="Index" header="#" style={{ width: '70px' }} body={rowIndexTemplate}></Column>
                        <Column field="title" header={'Title'}></Column>
                        <Column field="position" header={'position'}></Column>
                        <Column field="description" header={'Description'}></Column>

                        <Column header={labels.LABEL_STATUS} body={statusBodyTemplate}></Column>
                        <Column style={{ width: '120px' }} header="Actions" body={actionBodyTemplate}></Column>
                    </DataTable>

                    <Paginator first={first} rows={constants.MAXIMUM_RECORDS_PER_PAGE} totalRecords={totalItems} alwaysShow={true} onPageChange={onPageChange} template={paginatorTemplate} />
                </div>
            </div>
            <LessonFormDialog courseId={Number(courseId)} isOpen={openDialog} lookupTypes={lookupTypes} toggle={toggleOpenDialog} messageRef={message} record={selectedUser} reloadFn={fetchRecordsFromServer} />
        </div>
    );
};

export default LecturesTab;
