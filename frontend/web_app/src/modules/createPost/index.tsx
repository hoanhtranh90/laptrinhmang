import React, { useEffect, useState } from "react"

import { UploadOutlined } from "@ant-design/icons"
import { useDeleteFileMutation, useLazyGetFileByObjectIdAndTypeQuery, useUploadFileForObjectMutation, useUploadFileMutation } from "../../redux/api/FileApi";

import { Button, Col, Form, Input, message, Modal, notification, Row, Upload } from "antd"
import { fileTypes } from "../common/assets/ApiConst";
import { CreatePostRequest } from "./type";
import { useAddPostMutation } from "./redux/CreatePostApi";
import ConfirmModal from "../common/components/ConfirmModal";

interface Props {
    visible: boolean,
    toggleModal: () => void,
    onSuccess: () => void;
}

const { confirm } = Modal;
const imageTypes = ['image/png', 'image/jpeg', 'image/png'];

const CreatePost = ({ visible, toggleModal, onSuccess }: Props) => {
    const [form] = Form.useForm();
    const [deleteIds, setDeleteIds] = useState<number[]>([]);
    const [loading, setLoading] = useState(false);
    const [confirmVisible, setConfirmVisible] = useState(false);
    const [errorStatus, setErrorStatus] = useState(false);
    const [helper, setHelper] = useState("");


    const [uploadFile] = useUploadFileMutation();
    const [deleteFile] = useDeleteFileMutation();
    const [addPost] = useAddPostMutation();
    const [addImage] = useUploadFileForObjectMutation();

    const [fileIdTrigger] = useLazyGetFileByObjectIdAndTypeQuery();

    const toggleConfirm = () => {
        setConfirmVisible(!confirmVisible);
    }
    const onFileRemove = (file: any) => {
        return new Promise<boolean>((resolve, reject) => {
            confirm({
                title: 'Xoá file đã chọn?',
                onOk: () => {
                    if (file.response?.attachmentId) {
                        let newDeleteIds = deleteIds;
                        newDeleteIds.push(file.response.attachmentId)
                        setDeleteIds(newDeleteIds);
                    }
                    resolve(true);

                },
                onCancel: () => {
                    resolve(false)
                }
            })
        })
    }
    const customUpload = (options: any) => {
        options.onProgress({ percent: 0 })
        let formData = new FormData();
        formData.append('file', options.file);
        uploadFile({
            file: formData,
            objectType: fileTypes.POST,
            storageType: "",
        }).unwrap()
            .then(res => {
                options.onProgress({ percent: 100 });
                options.onSuccess(res, options.file);
            }).catch(err => {
                options.onError();
            })
    }

    const beforeUpload = (files: File[]) => {
        let accepted = true;
        Array.from(files).forEach(file => {
            if (!imageTypes.includes(file.type) || file.size / (1024 * 1024) > 5) {
                accepted = false;
                setErrorStatus(true);
                setHelper("Định dạng file không hợp lệ")
            } else {
                setErrorStatus(false);
                setHelper("");
            }
        })

        return accepted || Upload.LIST_IGNORE;
    }
    const normFile = (e: any) => {
        return e.fileList;
    };
    const onFinish = (values: any) => {
        toggleConfirm();
        setLoading(true);
        deleteIds.forEach(item => {
            deleteFile(item)
        })

        let imageIds = values.upload?.length ? values.upload.map((item: any) => item.response.attachmentId) : [];

        let postInfo: CreatePostRequest = {
            title: values.title,
            content: values.content,
        }
        console.log(postInfo);
        

        //chuyển data sag api
        addPost(postInfo).unwrap().then(res => {
            notification.success({
                message: "Thêm mới thành công!"
            })
            onSuccess();
            toggleModal();
            setLoading(false);
            addImageAfterSubmit(res.id, imageIds);
        }).catch(err => {
            notification.error({
                message: err.data?.message || "Đã có lỗi xảy ra, xin vui lòng thử lại sau!",
            })
            setLoading(false);
        })

    }
    const addImageAfterSubmit = (objectId: number, listFileIds: Array<number>) => {
        addImage({
            listFileIds: listFileIds,
            objectId: objectId,
            objectType: fileTypes.POST,
        })
            .unwrap()
            .then(() => {
                if (visible) {
                    toggleModal();
                }
                // notification.success({
                //     message: "Thành công."
                // })
            })
            .catch((err:any) => {
                notification.error({
                    message: err.data?.message || "Đã có lỗi xảy ra. Vui lòng thử lại sau!"
                })
            })
    }
    return (
        <Modal
            visible={visible}
            title="Thông tin banner 1"
            onCancel={() => toggleModal()}
            width={800}
            footer={[
                <React.Fragment key="footer">
                    <Button htmlType='button' onClick={toggleModal}>
                        Đóng
                    </Button>
                    <Button htmlType='button' type='primary' onClick={() => toggleConfirm()} loading={loading}>
                        Lưu
                    </Button>
                </React.Fragment>
            ]}
        >
            <Form
                name="searchBox"
                onFinish={onFinish}
                autoComplete="off"
                form={form}
                labelWrap
                labelCol={{ span: 6 }}
                wrapperCol={{ span: 18 }}
                labelAlign="left"
                requiredMark={false}
                colon={false}
            >

                <Form.Item
                    name="title"
                    label="Tên bài viết"
                    rules={[{ required: true, message: 'Vui lòng nhập tên bài viết' }]}
                >
                    <Input />

                </Form.Item>

                <Form.Item
                    name="content"
                    label="Mô tả bài viết"
                    rules={[{ required: true, message: 'Vui lòng nhập mô tả' }]}
                >
                    <Input />

                </Form.Item>

                <Form.Item
                    label="Hình ảnh"
                    name="upload"
                    valuePropName="fileList"
                    getValueFromEvent={normFile}
                    extra="(Vui lòng chọn một hình ảnh có định dạng jpg, jpeg, png có dung lượng nhỏ hơn 5MB)"
                    rules={[
                        { required: true, message: "Hình ảnh là bắt buộc, vui lòng nhập đầy đủ." }
                    ]}
                    labelCol={{ span: 3 }}
                    wrapperCol={{ span: 21 }}
                    validateStatus={errorStatus ? "error" : ""}
                    help={helper}
                >
                    <Upload
                        onRemove={(file) => onFileRemove(file)}
                        customRequest={(options) => customUpload(options)}
                        accept="image/jpg,image/jpeg,image/png"
                        beforeUpload={(file, fileList) => beforeUpload(fileList)}
                        name="image"
                        listType="picture-card"
                        showUploadList={{ showRemoveIcon: false }}
                        maxCount={5}
                    >
                        <Button icon={<UploadOutlined />} type="link" />
                    </Upload>
                </Form.Item>
                {confirmVisible ?
                    <ConfirmModal
                        cancelText="Huỷ bỏ"
                        okText="Đồng ý"
                        classBtnOk="ant-button-dangerous"
                        contents={["Bạn có chắc chắn muốn thay đổi thông tin không?"]}
                        handleCancel={toggleConfirm}
                        handleOk={() => form.submit()}
                        type={1}
                        visible={confirmVisible}
                        widthConfig={424} />
                    : <></>}
            </Form>

        </Modal>
    )
}
export default CreatePost;

