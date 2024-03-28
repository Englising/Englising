import React from 'react';
import SideBar from '../component/main/Sidebar';
import { Outlet } from 'react-router-dom';

const SidebarLayout = () => {
    return (
        <div>
            <div className="bg-black h-svh w-screen m-0 p-0 flex">
                <SideBar/>
                <Outlet/>
            </div>
        </div>
    );
};

export default SidebarLayout;