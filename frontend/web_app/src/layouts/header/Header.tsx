import React from 'react'
import { Col, Row, Typography, Space, Menu, Dropdown, Layout } from 'antd';
import { DownOutlined } from '@ant-design/icons';
import './assets/css/index.css';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../redux/Hooks';
import { logout, setUser } from '../../modules/login/redux/UserSlice';

const { Text } = Typography;
const { Header } = Layout;

const AppHeader = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const user = useAppSelector((state) => state.user)
  const dispatch = useAppDispatch();

  const logOut = () => {
    // localStorage.setItem("isLogin", "false");
    dispatch(logout());
    dispatch(setUser({}));
    navigate("/login", { replace: true, state: { from: location } })
  }

  const menu = (
    <Menu>
      <Menu.Item key='detail'>
        <Link to='/account/detail'>
          <span>Thông tin tài khoản</span>
        </Link>
      </Menu.Item>
      <Menu.Item key='change_pw'>
        <Link to='/account/change-password'>
          <span>Đổi mật khẩu</span>
        </Link>
      </Menu.Item>
      <Menu.Item onClick={logOut} key='logout'>
        <span>Đăng xuất</span>
      </Menu.Item>
    </Menu>
  )


  return (
    <Header className="header">
      <Row align='middle' justify='space-between'>
        <Col span={10}>
          <Row align='middle'>
            <Col>
              <div className='header-logo'></div>
            </Col>
            <Col>
              <div className='font-size-24'>
                <Text strong>MP3</Text>
              </div>
              
            </Col>
          </Row>
        </Col>
        <Col span={10}>
          <Row justify='end' align='middle'>
            <Space size={48}>
              {/* <Text className='font-size-18'>Hướng dẫn sử dụng</Text> */}
              <Dropdown overlay={menu} trigger={['click']}>
                <Text className='font-size-18 cursor-pointer'>{user.user.fullName} <DownOutlined /></Text>
              </Dropdown>
            </Space>
          </Row>
        </Col>
      </Row>
    </Header>
  )
}

export default AppHeader
