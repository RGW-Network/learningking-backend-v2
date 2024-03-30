import AppSubMenu from './AppSubMenu';
import type { MenuModel } from '@/types';

const AppMenu = () => {
    const model: MenuModel[] = [
        {
            label: 'Dashboard',
            icon: 'pi pi-home',
            to: '/'
        },
        {
            label: 'Course Mgt',
            icon: 'pi pi-fw pi-book',
            items: [
                {
                    label: 'Courses',
                    icon: 'pi pi-fw pi-table',
                    to: '/courses'
                },
                {
                    label: 'Categories',
                    icon: 'pi pi-fw pi-list',
                    to: '/course-categories'
                }
            ]
        },
        {
            label: 'Categories',
            icon: 'pi pi-fw pi-cog',
            to: '/course-categories'
        },
        {
            label: 'Lookups',
            icon: 'pi pi-fw pi-cog',
            to: '/lookups'
        },
        {
            label: 'Users',
            icon: 'pi pi-home',
            to: '/users'
        },

        {
            label: 'User Management',
            icon: 'pi pi-fw pi-user',
            items: [
                {
                    label: 'List',
                    icon: 'pi pi-fw pi-list',
                    to: '/users'
                }
            ]
        },
        {
            label: 'Hierarchy',
            icon: 'pi pi-fw pi-align-left',
            items: [
                {
                    label: 'Submenu 1',
                    icon: 'pi pi-fw pi-align-left',
                    items: [
                        {
                            label: 'Submenu 1.1',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 1.1.1',
                                    icon: 'pi pi-fw pi-align-left'
                                },
                                {
                                    label: 'Submenu 1.1.2',
                                    icon: 'pi pi-fw pi-align-left'
                                },
                                {
                                    label: 'Submenu 1.1.3',
                                    icon: 'pi pi-fw pi-align-left'
                                }
                            ]
                        },
                        {
                            label: 'Submenu 1.2',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 1.2.1',
                                    icon: 'pi pi-fw pi-align-left'
                                }
                            ]
                        }
                    ]
                },
                {
                    label: 'Submenu 2',
                    icon: 'pi pi-fw pi-align-left',
                    items: [
                        {
                            label: 'Submenu 2.1',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 2.1.1',
                                    icon: 'pi pi-fw pi-align-left'
                                },
                                {
                                    label: 'Submenu 2.1.2',
                                    icon: 'pi pi-fw pi-align-left'
                                }
                            ]
                        },
                        {
                            label: 'Submenu 2.2',
                            icon: 'pi pi-fw pi-align-left',
                            items: [
                                {
                                    label: 'Submenu 2.2.1',
                                    icon: 'pi pi-fw pi-align-left'
                                }
                            ]
                        }
                    ]
                }
            ]
        },
        {
            label: 'Start',
            icon: 'pi pi-fw pi-download',
            items: [
                {
                    label: 'Buy Now',
                    icon: 'pi pi-fw pi-shopping-cart',
                    url: 'https://www.primefaces.org/store'
                },
                {
                    label: 'Documentation',
                    icon: 'pi pi-fw pi-info-circle',
                    to: '/documentation'
                }
            ]
        }
    ];

    return <AppSubMenu model={model} />;
};

export default AppMenu;
