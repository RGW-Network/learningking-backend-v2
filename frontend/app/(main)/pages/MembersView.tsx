import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import React, { useState, useEffect, useRef } from "react";
import { Messages } from "primereact/messages";
import { Paginator } from "primereact/paginator";
import { useHistory } from "react-router";
import { Panel } from "primereact/panel";
import { BreadCrumb } from "primereact/breadcrumb";
import * as constants from "../app_utils/constants/Constants";
import { generatePath } from "react-router-dom";
import * as labels from "../app_utils/constants/Labels";
import useShowModalDialog from "../app_utils/components/ShowModalHook";
import {
  HOME_ROUTE_PATH,
  MEMBER_DETAILS_ROUTE_PATH,
} from "../app_utils/route_paths/resolver/PageRoutes";
import { PrimeIcons } from "primereact/api";
import { BaseApiServiceImpl } from "../app_utils/api/BaseApiServiceImpl";
import { MessageUtils } from "../app_utils/utils/MessageUtils";
import {
  generalStatusBodyTemplate,
  replaceWithUnderscore,
  sanitizeValue,
  toReadableDate,
} from "../app_utils/utils/Utils";
import { getFilterComponent } from "../app_utils/components/Filters";
import { paginatorTemplate } from "../app_utils/components/PaginatorTemplate";
import { filtersHeadertemplate } from "../app_utils/components/FiltersPanelHeader";
import MemberFormDialogView from "./MemberFormDialogView";
import UploadFormDialog from "./UploadFormDialog";

const MembersView = () => {
  const [records, setRecords] = useState<any>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [searchTermFilter, setSearchTermFilter] = useState<string | null>(null);
  const [recordStatusFilter, setRecordStatusFilter] = useState<string | null>(
    null
  );
  const [totalItems, setTotalItems] = useState<number>(0);
  const [first, setFirst] = useState<number>(0);
  const [limit, setLimit] = useState<number>(
    constants.MAXIMUM_RECORDS_PER_PAGE
  );
  const [openUploadDialog, setOpenUploadDialog] = useState<boolean>(false);

  const [selectedMember, setSelectedMember] = useState<any>(null);
  const [territories, setTerritories] = useState<any>(null);
  const [territoryFilter, setTerritoryFilter] = useState<any>(null);

  let offset = 0;
  let record = null;
  const message = useRef<any>();
  const history = useHistory();

  const { openDialog, toggleOpenDialog } = useShowModalDialog();

  /**
   * These are the bread crumbs that serve as the title of the page
   */
  const breadcrumbHome = {
    icon: "pi pi-home",
    command: () => {
      history.push(HOME_ROUTE_PATH);
    },
  };

  const breadcrumbItems = [
    {
      label: `Members`,
      icon: PrimeIcons.FLAG,
    },
  ];

  /**
   * This gets the parameters to submit in the GET request to back office
   * @returns
   */
  const getQueryParameters = () => {
    let searchParameters: any = { offset: offset, limit: limit };
    if (searchTermFilter !== null)
      searchParameters.searchTerm = searchTermFilter;
    if (recordStatusFilter !== null)
      searchParameters.recordStatus = recordStatusFilter;
    if (territoryFilter !== null)
      searchParameters.commaSeparatedTerritoryIds = territoryFilter;

    return searchParameters;
  };

  /**
   * This fetches counties from the back office using the search parameters
   */
  const fetchRecordsFromServer = () => {
    setIsLoading(true);
    let searchParameters: any = getQueryParameters();

    new BaseApiServiceImpl("/api/v1/members")
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
    setTerritoryFilter(territoryFilter);
    fetchRecordsFromServer();
  };

  /**
   * This is used to clear all filters and resubmit the get request to the back office
   */
  const resetFilters = () => {
    setSearchTermFilter("");
    setRecordStatusFilter(null);
    setTerritoryFilter(null);
    fetchRecordsFromServer();
  };

  /**
   * This opens the edit territory dialog form by toggling the open dialog variable
   */
  const openEditFormDialog = (rowData: any) => {
    setSelectedMember(rowData);
    record = rowData;
    toggleOpenDialog();
  };

  /**
   * This opens the new territory dialog form by toggling the open dialog variable
   * and setting the selected territory to null
   */
  const openNewFormDialog = () => {
    setSelectedMember(null);
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
          className="p-button-sm p-button-warning mr-2"
          onClick={() => {
            openEditFormDialog(rowData);
          }}
        />

        <Button
          icon={PrimeIcons.EYE}
          className="p-button-sm p-button-primary "
          onClick={() => {
            history.push(
              generatePath(MEMBER_DETAILS_ROUTE_PATH, { id: rowData.id })
            );
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
    return generalStatusBodyTemplate(rowData?.recordStatus);
  };

  /**
   * The date  row template
   * @param rowData
   * @returns
   */
  const dateTemplate = (rowData: any) => {
    return <>{toReadableDate(rowData.member.dateOfBirth, true)}</>;
  };
  const locationTemplate = (rowData: any) => {
    return (
      <>{`${sanitizeValue(rowData.member.village?.value)} , ${sanitizeValue(
        rowData.member.parish?.value
      )}`}</>
    );
  };
  const nameTemplate = (rowData: any) => {
    return <>{`${rowData?.member.firstName} ${rowData?.member.lastName}`}</>;
  };

  /**
   * The template for the filter buttons
   */
  const filterButtonsTemplate = (
    <>
      <div className="col-6  md:col-2 p-fluid" key="filterBtns">
        <Button
          icon={constants.ICON_SEARCH}
          className={constants.CSS_FILTER_SUBMIT_BUTTON}
          onClick={onSubmitFilter}
          loading={isLoading}
        />
        <Button
          icon={constants.ICON_REFRESH}
          className={constants.CSS_FILTER_RESET_BUTTON}
          onClick={resetFilters}
          loading={isLoading}
        />
      </div>
    </>
  );

  /**
   * This is a list of filters to display in the filter section
   */
  const filterDetails = [
    {
      type: "text",
      value: searchTermFilter,
      onChangeFn: setSearchTermFilter,
      id: "searchTermFilter",
      label: labels.LABEL_SEARCH_TERM,
      colWidth: constants.CSS_FILTER_SEARCH_INPUT_DIV,
      onkeydownFn: onSubmitFilter,
    },
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
  const toggleUploadDialog = () => {
    setOpenUploadDialog(!openUploadDialog);
  };
  return (
    <div className="grid">
      <div className="col-6 flex justify-content-start flex-wrap">
        <BreadCrumb home={breadcrumbHome} model={breadcrumbItems} />
      </div>
      <div className="col-6 flex justify-content-end flex-wrap">
        <Button
          label={"Create Member"}
          icon={PrimeIcons.PLUS}
          className="p-button-secondary mr-2 ml-2 mt-5"
          onClick={openNewFormDialog}
        />

        <Button
          icon={PrimeIcons.UPLOAD}
          className="p-button-primary mr-2 ml-2 mt-5"
          label="Upload Notes"
          onClick={toggleUploadDialog}
          loading={isLoading}
        />
      </div>
      <Messages ref={message} style={{ width: "100%" }} />
      <div className="col-12">
        <Panel headerTemplate={filtersHeadertemplate} toggleable>
          <div className="grid">
            {dynamicFilterDetails}
            {filterButtonsTemplate}
          </div>
        </Panel>
      </div>
      <div className="col-12">
        <div className="card">
          <DataTable
            responsiveLayout="stack"
            value={records}
            paginator={false}
            stripedRows
            size="normal"
            className="datatable-responsive"
            paginatorPosition="both"
            emptyMessage="No record found."
            loading={isLoading}
          >
            <Column
              field="Index"
              header="#"
              style={{ width: "70px" }}
              body={rowIndexTemplate}
            ></Column>
            <Column body={nameTemplate} header={"Full Name"}></Column>
            <Column field="member.phone" header={"Phone Number"}></Column>
            <Column field="member.ninOrRid" header={"NIN / ID"}></Column>
            <Column body={dateTemplate} header={"D.O.B"}></Column>
            <Column
              body={locationTemplate}
              header={"Village (Parish)"}
            ></Column>
            <Column field="groupName" header={"Group"}></Column>
            <Column
              style={{ width: "120px" }}
              header="Actions"
              body={actionBodyTemplate}
            ></Column>
          </DataTable>

          <Paginator
            first={first}
            rows={constants.MAXIMUM_RECORDS_PER_PAGE}
            totalRecords={totalItems}
            alwaysShow={true}
            onPageChange={onPageChange}
            template={paginatorTemplate}
          />
        </div>
      </div>
      <MemberFormDialogView
        isOpen={openDialog}
        toggle={toggleOpenDialog}
        messageRef={message}
        memberObject={selectedMember}
        reloadFn={fetchRecordsFromServer}
      ></MemberFormDialogView>
      {openUploadDialog && (
        <UploadFormDialog
          isOpen={openUploadDialog}
          toggleOpen={toggleUploadDialog}
          reloadFn={fetchRecordsFromServer}
        ></UploadFormDialog>
      )}
    </div>
  );
};

export default MembersView;
