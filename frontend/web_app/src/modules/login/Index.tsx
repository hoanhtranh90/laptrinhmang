import { Alert, Button, Checkbox, Col, Form, Input, Row, Typography } from 'antd';
import { Link, Navigate, useLocation } from 'react-router-dom';
import './assets/css/index.css';
import { useEffect, useState } from 'react';
import { useLoginMutation } from './redux/LoginApi';
import { useAppDispatch, useAppSelector } from '../../redux/Hooks';
import { setToken } from './redux/UserSlice';
import { isTokenExpired } from '../common/assets/CommonFunctions';

const { Title } = Typography;

const Login = () => {
    const location = useLocation();
    const [isLogin, setIsLogin] = useState(false);
    const [login, { isLoading, error }] = useLoginMutation();
    const user = useAppSelector((state) => state.user);
    const dispatch = useAppDispatch();

    useEffect(() => {
        let accessToken = localStorage.getItem("accessToken");
        console.log('Failed:', accessToken)

        if (accessToken && !isTokenExpired(accessToken)) {
            setIsLogin(true);
            dispatch(setToken(accessToken));
        }
    }, [dispatch])

    useEffect(() => {
        console.log('Failed:12', user)

        if (user.userToken && !isTokenExpired(user.userToken)) {
            localStorage.setItem("accessToken", user.userToken);
            setIsLogin(true);
        }
    }, [user])

    const onFinish = (values: any) => {
        login({ uname: values.username, pwd: values.password })
    };

    const onFinishFailed = (errorInfo: any) => {
        console.log('Failed:', errorInfo);
    };

    if (isLogin) {
        return <Navigate to="/home" replace state={{ from: location }} />
    }

    return (
        <div className='login-background'>
            <Row align='middle' justify='center'>
                <Col span={8} offset={16} className='login-card'>
                    {/* <div style={{ display: 'flex', flexDirection: 'column', height: '100%', alignContent: 'center', justifyItems: 'center' }}> */}
                    <Title level={2}>ĐĂNG NHẬP</Title>
                    <Alert message={user?.errorMessage || "Đã có lỗi xảy ra"} type="error" style={{ marginBottom: 10, visibility: error ? 'visible' : 'hidden' }} />
                    <Form
                        name="loginForm"
                        labelCol={{ span: 8 }}
                        wrapperCol={{ span: 16 }}
                        initialValues={{ remember: false }}
                        onFinish={onFinish}
                        onFinishFailed={onFinishFailed}
                        autoComplete="off"
                        requiredMark={false}
                        validateTrigger="onSubmit"
                    >
                        <Form.Item
                            labelCol={{ span: 9 }}
                            labelAlign='left'
                            label="Tên đăng nhập"
                            name="username"
                            rules={[{ required: true, message: 'Tên đăng nhập là bắt buộc, vui lòng nhập đầy đủ.' }]}
                        >
                            <Input style={{ height: 50 }} placeholder="Tên đăng nhập" maxLength={50} />
                        </Form.Item>

                        <Form.Item
                            labelCol={{ span: 9 }}
                            labelAlign='left'
                            label="Mật khẩu"
                            name="password"
                            rules={[{ required: true, message: 'Mật khẩu là bắt buộc, vui lòng nhập đầy đủ.' }]}
                        >
                            <Input.Password style={{ height: 50 }} placeholder="Mật khẩu" maxLength={20} />
                        </Form.Item>

                        <Form.Item style={{ justifyContent: 'end' }}>
                            <Row gutter={48}>
                                <Col span={12}>
                                    {!isLoading ?
                                        <Button type="primary" htmlType="submit">
                                            Đăng nhập
                                        </Button> :
                                        <Button type='primary' loading>Đăng nhập</Button>}
                                </Col>
                                <Col span={12}>
                                    <Button  >

                                        <Link to='/register'>
                                            <span>Đăng ký</span>
                                        </Link>
                                    </Button>
                                </Col>

                            </Row>
                            <Row gutter={48} style={{marginTop:"20px"}}>
                                <Col span={12}>
                                    <Button  >

                                        <Link to='/forget-pass'>
                                            <span>Quên mật khẩu</span>
                                        </Link>
                                    </Button>
                                </Col>
                            </Row>
                        </Form.Item>
                    </Form>

                    {/* </div> */}

                </Col>
            </Row>
        </div >
    )
}

export default Login;