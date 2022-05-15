import { Alert, Button, Checkbox, Col, Form, Input, notification, Row, Typography } from 'antd';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import './assets/css/index.css';
import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/Hooks';
import { isTokenExpired } from '../common/assets/CommonFunctions';
import { useRegisterMutation } from './redux/RegisterApi';

const { Title } = Typography;

const Register = () => {
    const location = useLocation();
    const [isLogin, setIsLogin] = useState(false);
    const user = useAppSelector((state) => state.user);
    const dispatch = useAppDispatch();
    const [register] = useRegisterMutation();
    const navigate = useNavigate();
    const onFinish = (values: any) => {
        register({ 
            userName: values.username, 
            password: values.password,
            email: values.email,
            phoneNumber: values.phoneNumber, 
            fullName: values.fullName,
        }).then(res => {
            console.log(res);
            notification.success({
                message: "Đăng ký thành công!",
            })
            //redirect to login page with ts
            navigate('/login');
        });
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
                    <Title level={2}>ĐĂNG KÝ</Title>
                    {/* <Alert message={user?.errorMessage || "Đã có lỗi xảy ra"} type="error" style={{ marginBottom: 10, visibility: error ? 'visible' : 'hidden' }} /> */}
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
                            label="Họ và tên"
                            name="fullName"
                            rules={[{ required: true, message: 'Tên hiển thị bắt buộc' }]}
                        >
                            <Input style={{ height: 50 }} placeholder="Tên hiển thị" maxLength={50} />
                        </Form.Item>

                        {/* email */}
                        <Form.Item
                            labelCol={{ span: 9 }}
                            labelAlign='left'
                            label="Email"
                            name="email"
                            rules={[{ required: true, message: 'Email là bắt buộc.' }]}
                        >
                            <Input style={{ height: 50 }} placeholder="Email" maxLength={50} />
                        </Form.Item>

                        <Form.Item
                            labelCol={{ span: 9 }}
                            labelAlign='left'
                            label="Số điện thoại"
                            name="phoneNumber"
                        >
                            <Input style={{ height: 50 }} placeholder="Số điện thoại" maxLength={50} />
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
                                    <Button  htmlType="submit">
                                           Đăng ký
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

export default Register;