import { Col, Layout, Menu, Row } from 'antd';
import { ReactComponent as Clipboard } from '../../assets/logos/clipboard-list.svg';
import { ReactComponent as Folder } from '../../assets/logos/folder-open.svg';
import { ReactComponent as DocumentCopy } from '../../assets/logos/document-copy.svg';
import { ReactComponent as Group } from '../../assets/logos/clarity_group-solid.svg';
import { ReactComponent as Auction } from '../../assets/logos/auction.svg';
import { ReactComponent as Storage } from '../../assets/logos/sd-storage.svg';
import { ReactComponent as BarChart } from '../../assets/logos/bar-chart.svg';
import { ReactComponent as File } from '../../assets/logos/file-menu.svg';
import { ReactComponent as Settings } from '../../assets/logos/settings.svg';
import { ReactComponent as Home } from '../../assets/logos/home.svg';

import Icon, { AlignLeftOutlined, DoubleRightOutlined, HddOutlined, MenuFoldOutlined, MenuUnfoldOutlined, ShopOutlined } from '@ant-design/icons';
import './assets/css/index.css';
import { useState } from 'react';
import { Link } from 'react-router-dom';

const { Sider } = Layout;
const { SubMenu } = Menu;

const HomeIcon = () => <Icon component={Home} style={{ fontSize: 18, color: 'black' }} />
const ClipboardIcon = () => <Icon component={Clipboard} style={{ fontSize: 18, color: 'black' }} />
const FolderIcon = () => <Icon component={Folder} style={{ fontSize: 18, color: 'black' }} />
const DocumentCopyIcon = () => <Icon component={DocumentCopy} style={{ fontSize: 18, color: 'black' }} />
const GroupIcon = () => <Icon component={Group} style={{ fontSize: 18, color: 'black' }} />
const AuctionIcon = () => <Icon component={Auction} style={{ fontSize: 18, color: 'black' }} />
const StorageIcon = () => <Icon component={Storage} style={{ fontSize: 18, color: 'black' }} />
const BarChartIcon = () => <Icon component={BarChart} style={{ fontSize: 18, color: 'black' }} />
const FileIcon = () => <Icon component={File} style={{ fontSize: 18, color: 'black' }} />
const SettingsIcon = () => <Icon component={Settings} style={{ fontSize: 18, color: 'black' }} />

type Props = {
    headerHeight: number;
}

const Sidebar = ({ headerHeight }: Props) => {
    const [collapsed, setCollapsed] = useState(false);

    const onCollapse = () => {
        setCollapsed(!collapsed);
    }

    return (
        <Sider width={300} className='sidebar' collapsible collapsed={true} trigger={null}>
            {/* <Row align='middle' justify={collapsed ? 'center' : 'space-between'} className='sidebar-header text-center'>
                <Col style={{ display: collapsed ? 'none' : 'block' }}>MENU</Col> 
                <Col>
                    {!collapsed ?
                        <MenuFoldOutlined onClick={onCollapse} style={{ fontSize: 18 }} /> :
                        <MenuUnfoldOutlined onClick={onCollapse} style={{ fontSize: 18 }} />}
                </Col>
            </Row>*/}
            <Menu mode='inline' style={{ height: `calc(100vh - 130px)`, overflow: 'auto' }}>
                <Menu.Item key='home' icon={<HomeIcon />}>
                    <Link to="/home">
                        Trang chủ
                    </Link>
                </Menu.Item>
                {/* <SubMenu key='contract' title='Quản lý' icon={<AlignLeftOutlined />}>
                    <Menu.Item key='contract-detail'>
                        <Link to='/follow'>
                            <DoubleRightOutlined /> Theo dõi 
                        </Link>
                    </Menu.Item>
                    
                    
                </SubMenu> */}
                <Menu.Item key='follow' icon={<AlignLeftOutlined />}>
                    <Link to='/follow'>
                        Theo dõi
                    </Link>
                </Menu.Item>
                {/* <Menu.Item key='work' icon={<HddOutlined />}>
                    <Link to='/work'>
                        Công việc
                    </Link>
                </Menu.Item> */}
                <Menu.Item key='livestream' icon={<ShopOutlined />}>
                    <Link to='/live'>
                        LiveStream
                    </Link>
                </Menu.Item>
            </Menu>
        </Sider>
    )
}

export default Sidebar;