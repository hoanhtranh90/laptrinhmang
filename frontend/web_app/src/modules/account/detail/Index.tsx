import { LoadingOutlined, PlusOutlined } from "@ant-design/icons";
import { Breadcrumb, Button, Col, Form, Input, message, Row, Select, Space, Upload, notification } from "antd"
import { useEffect, useState } from "react";
import { Link } from "react-router-dom"
import { DatePicker } from "../../../custom-components";
import { useLazyGetUserInfoQuery } from "../../../redux/api/AccountApi";
import { fileTypes } from "../../common/assets/ApiConst";
import { useLazyGetFileByObjectIdAndTypeQuery, useUploadFileForObjectMutation, useUploadFileMutation } from "../../../redux/api/FileApi";
import { useUpdateDetailMutation } from "./redux/AccountDetailApi";
import { User } from "../../../redux/api/apiTypes";
import ConfirmationModal from "../../common/components/ConfirmationModalSimple";

const { Option } = Select;

type FieldCount = {
    nameCount: number,
    emailCount: number,
    phoneCount: number,
    positionCount: number,
    addressCount: number,
}

const defaultCount: FieldCount = {
    nameCount: 0,
    emailCount: 0,
    phoneCount: 0,
    positionCount: 0,
    addressCount: 0,
}

const AccountDetail = () => {
    const [form] = Form.useForm();

    const [trigger] = useLazyGetUserInfoQuery();
    const [uploadFile] = useUploadFileMutation();
    const [fileIdTrigger] = useLazyGetFileByObjectIdAndTypeQuery();
    const [updateDetail] = useUpdateDetailMutation();
    const [addAvatar] = useUploadFileForObjectMutation();

    const [count, setCount] = useState<FieldCount>(defaultCount);

    const [fileList, setFileList] = useState<any>([]);
    const [loading, setLoading] = useState(true);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        trigger({}).unwrap()
            .then(res => {
                form.setFieldsValue({
                    fullName: res.fullName,
                    email: res.email,
                    phone: res.phoneNumber,
                    detailAddress: res.address,
                    sex: res.sex,
                    userName: res.userName,
                    dateOfBirth: res.dateOfBirth
                })

                setCount({
                    nameCount: res.fullName?.length || 0,
                    emailCount: res.email?.length || 0,
                    phoneCount: res.phoneNumber?.length || 0,
                    addressCount: res.address?.length || 0,
                    positionCount: 0,
                })


                fileIdTrigger({ objectId: res.userId!, objectType: fileTypes.ACCOUNT }).unwrap()
                    .then(res => {
                        console.log('Failed:', res)
                        if (res && res.length) {
                            fetch(process.env.REACT_APP_API_URL + "/files/downloadFile/" + res[0].attachmentId, {
                                headers: {
                                    Authorization: 'Bearer ' + localStorage.getItem("accessToken"),
                                }
                            }).then(response => {
                                return response.blob().then(blob => {
                                    let reader = new FileReader();
                                    reader.readAsDataURL(blob);
                                    reader.onload = () => {
                                        setLoading(false);
                                        setFileList([{
                                            uid: res[0].attachmentId,
                                            name: res[0].fileName,
                                            status: 'done',
                                            thumbUrl: reader.result,
                                            response: res[0]
                                        }]);
                                    }
                                })
                            })
                        }
                    }).catch((err) => {
                        console.error(err);
                    })
            });


    }, [trigger, form, fileIdTrigger])

    useEffect(() => {
        if (fileList.length && form) {
            form.setFieldsValue({
                avatar: fileList || [],
            })
        }
    }, [fileList, form])

    const onValuesChange = (changedValues: any, allValues: any) => {
        console.log(allValues);
        setCount({
            ...count,
            nameCount: allValues.fullName?.length || 0,
            emailCount: allValues.email?.length || 0,
            phoneCount: allValues.phone?.length || 0,
            positionCount: allValues.position?.length || 0,
            addressCount: allValues.detailAddress?.length || 0
        })
    }

    const customUpload = (options: any) => {
        options.onProgress({ percent: 0 })
        let formData = new FormData();
        formData.append('file', options.file);
        uploadFile({
            file: formData,
            objectType: fileTypes.ACCOUNT,
        }).unwrap()
            .then(res => {
                options.onProgress({ percent: 100 });
                options.onSuccess(res, options.file);
                fetch(process.env.REACT_APP_API_URL + "/files/downloadFile/" + res.attachmentId, {
                    headers: {
                        Authorization: 'Bearer ' + localStorage.getItem("accessToken"),
                    }
                }).then(response => {
                    return response.blob().then(blob => {
                        let reader = new FileReader();
                        reader.readAsDataURL(blob);
                        reader.onload = () => setFileList([
                            {
                                uid: res.attachmentId,
                                name: res.fileName,
                                status: 'done',
                                thumbUrl: reader.result,
                                response: res
                            }]);
                    })
                })

            }).catch(err => {
                options.onError();
            })
    }

    const normFile = (e: any) => {
        return e.fileList;
    };

    const beforeUpload = (files: File[]) => {
        let accepted = true;
        let fileList = [...files];
        fileList = fileList.slice(-1);

        Array.from(fileList).forEach(file => {
            if (file.type !== "image/png" && file.type !== "image/jpeg" && file.type !== "image/jpg") {
                accepted = false;
                message.error('?????nh d???ng file kh??ng h???p l???!');
            }

            if (file.size / (1024 * 1024) > 5) {
                accepted = false;
                message.error('Dung l?????ng file kh??ng h???p l???');
            }
        })

        return accepted || Upload.LIST_IGNORE;
    }

    const onFinish = (values: any) => {
        updateDetail({
            fullName: values.fullName?.trim() || "",
            email: values.email?.trim() || "",
            userId: values.userId,
            userName: values.userName?.trim() || "",
            phoneNumber: values.phone?.trim() || "",
            address: values.detailAddress?.trim() || "",
            provinceId: values.address?.province || 0,
            communeId: values.address?.ward || 0,
            districtId: values.address?.address || 0,
            sex: values.sex,
            dateOfBirth: values.dateOfBirth || "",
        }).unwrap()
            .then(res => {
                addImageAfterSubmit(res, values);
            })
            .catch(err => {
                notification.error({
                    message: err.data?.message || "???? c?? l???i x???y ra. Vui l??ng th??? l???i sau!"
                })
            })
    }

    const addImageAfterSubmit = (res: User, values: any) => {
        let imageId = values.avatar?.[0]?.response?.attachmentId;

        addAvatar({
            listFileIds: [imageId],
            objectId: res.userId!,
            objectType: fileTypes.ACCOUNT,
        })
            .unwrap()
            .then(() => {
                if (visible) {
                    toggleModal();
                }
                notification.success({
                    message: "C???p nh???t th??ng tin t??i kho???n th??nh c??ng."
                })
            })
            .catch((err) => {
                notification.error({
                    message: err.data?.message || "???? c?? l???i x???y ra. Vui l??ng th??? l???i sau!"
                })
            })
    }

    const toggleModal = () => {
        setVisible(!visible);
    }

    return (
        <>
            <Breadcrumb separator=">" style={{ marginLeft: 20, marginBottom: 20 }}>
                <Breadcrumb.Item>
                    <Link to='/home'>
                        Trang ch???
                    </Link>
                </Breadcrumb.Item>
                <Breadcrumb.Item>
                    Th??ng tin t??i kho???n
                </Breadcrumb.Item>
            </Breadcrumb>
            <label className='title-label'>Th??ng tin t??i kho???n</label>

            <div className='content-box'>
                <label className='search-title'> Th??ng tin ng?????i d??ng </label>
                <Form
                    name="accountDetail"
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
                    colon={false}
                    onValuesChange={onValuesChange}
                >
                    <Form.Item hidden name="userName"><Input hidden /></Form.Item>
                    <Row gutter={48} align='middle' justify="center">
                        <Col span={12}>
                            <Row style={{ width: '100%' }}>
                                <Col span={24}>
                                    {/* <Row justify="end"><span>{count.nameCount} / 50</span></Row> */}
                                    <Form.Item
                                        labelCol={{ span: 6 }}
                                        label="H??? v?? t??n"
                                        name="fullName"
                                        wrapperCol={{ span: 18 }}
                                        rules={[
                                            { required: true, whitespace: true, message: "H??? v?? t??n l?? b???t bu???c, vui l??ng nh???p ?????y ?????." }
                                        ]}
                                    >
                                        <Input showCount placeholder="Nh???p h??? v?? t??n" maxLength={50} />
                                    </Form.Item>
                                </Col>
                            </Row>
                            <Row style={{ width: '100%' }}>
                                <Col span={24}>
                                    <Row justify="end"><span>{count.emailCount} / 50</span></Row>
                                    <Form.Item
                                        labelCol={{ span: 6 }}
                                        label="Email"
                                        name="email"
                                        wrapperCol={{ span: 18 }}
                                        // rules={[
                                        //     { required: true, whitespace: true, message: "Email l?? b???t bu???c, vui l??ng nh???p ?????y ?????." }
                                        // ]}
                                    >
                                        <Input placeholder="Nh???p email" maxLength={50} />
                                    </Form.Item>
                                </Col>
                            </Row>
                            <Row style={{ width: '100%' }}>
                                <Col span={24}>
                                    <Row justify="end"><span>{count.phoneCount} / 10</span></Row>
                                    <Form.Item
                                        labelCol={{ span: 6 }}
                                        label="S??? ??i???n tho???i"
                                        name="phone"
                                        wrapperCol={{ span: 18 }}
                                        // rules={[
                                        //     { required: true, whitespace: true, message: "S??? ??i???n tho???i l?? b???t bu???c, vui l??ng nh???p ?????y ?????." }
                                        // ]}
                                    >
                                        <Input placeholder="Nh???p s??? ??i???n tho???i" maxLength={10} />
                                    </Form.Item>
                                </Col>
                            </Row>
                        </Col>
                        <Col span={12}>
                            <Row justify="center" align="middle">
                                <Col>
                                    <Form.Item
                                        valuePropName="fileList"
                                        getValueFromEvent={normFile}
                                        name='avatar'
                                    >
                                        <Upload
                                            listType="picture-card"
                                            className="avatar-uploader"
                                            showUploadList={false}
                                            customRequest={(options) => customUpload(options)}
                                            accept="image/jpg,image/jpeg,image/png"
                                            beforeUpload={(file, fileList) => beforeUpload(fileList)}
                                        >
                                            {fileList[0]?.thumbUrl ? <img src={fileList[0].thumbUrl} alt="avatar" style={{ width: '100%' }} /> :
                                                (<div>
                                                    {loading ? <LoadingOutlined /> : <PlusOutlined />}
                                                    <div style={{ marginTop: 8 }}>Upload</div>
                                                </div>)}
                                        </Upload>
                                    </Form.Item>
                                </Col>
                            </Row>
                        </Col>
                    </Row>

                    <label className='title-label' style={{ marginLeft: 0, fontSize: 16 }}>Th??ng tin th??m</label>

                    <Row gutter={48} style={{ marginTop: 30 }}>
                        <Col span={12}>
                            <Row justify="end"><span>{count.positionCount} / 50</span></Row>
                            <Form.Item
                                labelCol={{ span: 6 }}
                                label="Ch???c danh"
                                name="position"
                                wrapperCol={{ span: 18 }}
                            >
                                <Input placeholder="Nh???p ch???c danh" maxLength={50} />
                            </Form.Item>
                        </Col>
                    </Row>
                    <Row gutter={48}>
                        <Col span={12}>
                            <Form.Item
                                labelCol={{ span: 6 }}
                                label="Gi???i t??nh"
                                name="sex"
                                wrapperCol={{ span: 18 }}
                            >
                                <Select
                                    placeholder="Ch???n gi???i t??nh">
                                    <Option value={0}>Nam</Option>
                                    <Option value={1}>N???</Option>
                                    <Option value={2}>Kh??c</Option>
                                </Select>
                            </Form.Item>
                        </Col>
                        <Col span={12}>
                            <Form.Item
                                labelCol={{ span: 6 }}
                                label="Ng??y sinh"
                                name="dateOfBirth"
                                wrapperCol={{ span: 18 }}
                            >
                                <DatePicker placeholder="Ch???n ng??y sinh" format="dd/MM/yyyy" />
                            </Form.Item>
                        </Col>
                    </Row>

                    

                    <Row gutter={48}>
                        <Col span={24}>
                            <Row justify="end"><span>{count.addressCount} / 200</span></Row>
                            <Form.Item
                                name="detailAddress"
                                label="?????a ch??? c??? th???"
                                labelCol={{ span: 3 }}
                                wrapperCol={{ span: 21 }}>
                                <Input placeholder="Nh???p ?????a ch??? c??? th???" maxLength={200} className="clear-input" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row align="middle" justify="center">
                        <Col>
                            <Button htmlType='button' type='primary' onClick={toggleModal}>C???p nh???t</Button>
                        </Col>
                    </Row>

                    <ConfirmationModal
                        visible={visible}
                        message="B???n c?? ch???c ch???n mu???n c???p nh???t th??ng tin c?? nh??n kh??ng 1 2 3 ?"
                        toggleModal={toggleModal}
                        modalAction={() => form.submit()}
                    />
                </Form>
            </div>

        </>
    )
}

export default AccountDetail