import AppHeader from './header/Header';
import Sidebar from './sidebar/Sidebar';
import { Layout } from 'antd';
import { Outlet } from 'react-router-dom';
import { useEffect, useRef, useState } from 'react';

const { Content } = Layout;

const AppLayout = () => {
    const ref = useRef<any>();
    const [headerHeight, setHeaderHeight] = useState(0);

    useEffect(() => {
        if(ref?.current) {
            setHeaderHeight(ref.current.clientHeight);
        }
    }, [])

    return (
        <Layout style={{ minHeight: '100vh' }}>
            <div ref={ref}>
                <AppHeader />
            </div>
            <Layout>
                <Sidebar headerHeight={headerHeight} />
                <Layout>
                    <Content className='layout-content' style={{ height: `calc(100vh - ${headerHeight}px)`, overflow: 'auto' }}><Outlet /></Content>
                </Layout>
            </Layout>
        </Layout>
    )
}

export default AppLayout;