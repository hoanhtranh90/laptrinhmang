import { Breadcrumb, Button, Col, Form, Input, notification, Row } from "antd"
import { useEffect, useState } from "react";
import { Link } from "react-router-dom"
import { useLazyGetUserInfoQuery } from "../../../redux/api/AccountApi";
import { useChangePasswordMutation } from "./redux/ChangePasswordApi";

const pattern = /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$/;
const patternMessage = "Nhập mật khẩu tối thiểu 8 ký tự bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt.";

const ChangePassword = () => {
    const [form] = Form.useForm();
    const [changePass] = useChangePasswordMutation();
    const [trigger, response] = useLazyGetUserInfoQuery();

    const [oldPassCount, setOldPassCount] = useState(0);
    const [newPassCount, setNewPassCount] = useState(0);
    const [confirmPassCount, setConfirmPassCount] = useState(0);


    useEffect(() => {
        trigger({});
    }, [trigger])

    const onValuesChange = (values: any) => {
        if (Object.keys(values)?.[0] === "oldPass") {
            setOldPassCount(values.oldPass?.length || 0);
        }
        if (Object.keys(values)?.[0] === "newPass") {
            setNewPassCount(values.newPass?.length || 0);
        }
        if (Object.keys(values)?.[0] === "confirmPass") {
            setConfirmPassCount(values.confirmPass?.length || 0);
        }
    }

    const onFinish = (values: any) => {
        if (response.data?.userId) {
            changePass({ ...values, userId: response.data.userId }).unwrap()
                .then(res => {
                    form.resetFields();
                    notification.success({
                        message: "Thay đổi mật khẩu thành công!",
                    })
                }).catch(err => {
                    notification.error({
                        message: err.data?.message || "Thay đổi mật khẩu thất bại!"
                    })
                })
        }
    }

    return (
        <>
            <Breadcrumb separator=">" style={{ marginLeft: 20, marginBottom: 20 }}>
                <Breadcrumb.Item>
                    <Link to='/home'>
                        Trang chủ
                    </Link>
                </Breadcrumb.Item>
                <Breadcrumb.Item>
                    Thay đổi mật khẩu
                </Breadcrumb.Item>
            </Breadcrumb>
            <label className='title-label'>Thông tin tài khoản</label>

            <div className='content-box'>
                <Row justify="center" align="middle">
                    <Col>
                        <label className='search-title' style={{ marginLeft: 0 }}> Thông tin người dùng </label>
                    </Col>
                </Row>
                <Form
                    name="changePassword"
                    onFinish={onFinish}
                    //   onFinishFailed={onFinishFailed}
                    autoComplete="off"
                    layout="horizontal"
                    form={form}
                    labelWrap
                    labelAlign='left'
                    // className="custom-form"
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 18 }}
                    onValuesChange={onValuesChange}
                >
                    <Row gutter={48} align='middle' justify="center">
                        <Col span={14}>
                            <Row justify="end"><span>{oldPassCount} / 20</span></Row>
                            <Form.Item
                                label="Mật khẩu hiển tại"
                                name="oldPass"
                                rules={[
                                    { required: true, message: "Mật khẩu hiện tại là bắt buộc, vui lòng nhập đầy đủ." }
                                ]}
                            >
                                <Input.Password maxLength={20} />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={48} align='middle' justify="center">
                        <Col span={14}>
                            <Row justify="end"><span>{newPassCount} / 20</span></Row>
                            <Form.Item
                                label="Mật khẩu mới"
                                name="newPass"
                                rules={[
                                    { required: true, message: "Mật khẩu mới là bắt buộc, vui lòng nhập đầy đủ." },
                                    { pattern, message: patternMessage }
                                ]}
                            >
                                <Input.Password maxLength={20} />
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={48} align='middle' justify="center">
                        <Col span={14}>
                            <Row justify="end"><span>{confirmPassCount} / 20</span></Row>
                            <Form.Item
                                label="Nhập lại mật khẩu mới"
                                name="confirmPass"
                                dependencies={['newPass']}
                                hasFeedback
                                rules={[
                                    {
                                        required: true,
                                        message: 'Vui lòng nhập lại mật khẩu mới!',
                                    },
                                    ({ getFieldValue }) => ({
                                        validator(_, value) {
                                            if (!value || getFieldValue('newPass') === value) {
                                                return Promise.resolve();
                                            }
                                            return Promise.reject(new Error('Mật khẩu không trùng với mật khẩu mới!'));
                                        },
                                    }),
                                ]}
                            >
                                <Input.Password maxLength={20} />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row align="middle" justify="center">
                        <Col>
                            <Button htmlType='submit' type='primary'>Đổi mật khẩu</Button>
                        </Col>
                    </Row>
                </Form>
            </div>

        </>
    )
}

export default ChangePassword