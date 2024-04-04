'use client';
import React, { useEffect, useState } from 'react';
import { BaseApiServiceImpl } from '@/app/api/BaseApiServiceImpl';
import { MessageUtils } from '@/app/utils/MessageUtils';
import { isNotEmpty } from '@/app/utils/Utils';
import { useParams } from 'next/navigation';

import { TabView, TabPanel } from 'primereact/tabview';
import FormTab from './FormTab';
import LessonsTab from './LessonsTab';
import TopicsTab from './TopicsTab';

function CourseForm() {
    const pathParam = useParams();

    const recordId = pathParam.id;
    console.log('Current Record Id ' + recordId);
    const [course, setCourse] = useState<any>({});

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
    }, []);

    return (
        <div className="card">
            <span className="block text-900 font-bold text-xl mb-4">Create Course</span>
            <div className="grid grid-nogutter flex-wrap gap-3 p-fluid">
                <TabView>
                    <TabPanel header="Form">
                        <FormTab />
                    </TabPanel>
                    <TabPanel header="Lessons">
                        <LessonsTab />
                    </TabPanel>
                    <TabPanel header="Topics">
                        <TopicsTab />
                    </TabPanel>
                    <TabPanel header="Lectures">
                        <p>N/A</p>
                    </TabPanel>
                    <TabPanel header="Testimonials">
                        <p>N/A</p>
                    </TabPanel>
                </TabView>
            </div>
        </div>
    );
}

export default CourseForm;
