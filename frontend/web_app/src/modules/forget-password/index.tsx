import { Alert, Button, Checkbox, Col, Form, Input, notification, Row, Typography } from 'antd';
import { Link, Navigate, useLocation, useNavigate } from 'react-router-dom';
import './assets/css/index.css';
import { useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../redux/Hooks';
import { isTokenExpired } from '../common/assets/CommonFunctions';
import { useValidateOtpAndChangePassMutation, useGenOTPMutation } from './redux/ForgetPassApi';

const { Title } = Typography;

const ForgetPass = () => {
    const location = useLocation();
    const [isLogin, setIsLogin] = useState(false);
    const user = useAppSelector((state) => state.user);
    const dispatch = useAppDispatch();
    const [confirm] = useValidateOtpAndChangePassMutation();
    const [genOTP] = useGenOTPMutation();
    const navigate = useNavigate();
    const [form] = Form.useForm();
    const onFinish = (values: any) => {
        confirm({ 
            email: values.email,
            otp: values.otp,
            confirmPassword: values.password,
            newPassword: values.password
        }).then(res => {
            console.log(res);
            notification.success({
                message: "thành công!",
            })
            //redirect to login page with ts
            navigate('/login');
        });
    };
    const sendOTP = () => {
        console.log("hello world", form);
        genOTP({
            email: form.getFieldValue('email')
        }).then(res => {
            //@ts-ignore
            if(res && res?.error){
                notification.error({
                    // @ts-ignore
                    message: res?.error.data.message,
                })
            }
            else notification.success({
                message: "thành công!",
            })
            // navigate('/login');
        }).catch(err => {
            notification.error({
                message: err.data?.message || err.data.message
            })
        })
        
        
    }
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
                    <Title level={2}>Quên mật khẩu</Title>
                    {/* <Alert message={user?.errorMessage || "Đã có lỗi xảy ra"} type="error" style={{ marginBottom: 10, visibility: error ? 'visible' : 'hidden' }} /> */}
                    <Form
                        form={form}
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
                            label="Email"
                            name="email"
                        >
                            <Input style={{ height: 50 }} placeholder="Email" maxLength={50}  />
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
                        <Form.Item
                            labelCol={{ span: 9 }}
                            labelAlign='left'
                            label="Otp"
                            name="otp"
                        >
                            <Input style={{ height: 50 }} placeholder="OTP" maxLength={50} />
                        </Form.Item>
                        <Form.Item style={{ justifyContent: 'end' }}>
                            <Row gutter={48}>
                            <Col span={12}>
                                    <Button onClick={sendOTP}>
                                           Gửi OTP
                                    </Button>
                                </Col>
                                <Col span={12}>
                                    <Button  htmlType="submit">
                                           Xác nhận
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

export default ForgetPass;