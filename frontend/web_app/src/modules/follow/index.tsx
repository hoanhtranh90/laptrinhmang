import React, { useState, useEffect, useRef } from 'react'
import { Form, Row, Col, Input, Button, Table, Pagination, Select, notification, Breadcrumb, Menu, Dropdown, Space } from 'antd'
import { SettingOutlined } from '@ant-design/icons';

import { ColumnsType } from "antd/es/table";
import "../../assets/css/styles.scss";
import { Link } from 'react-router-dom';
import { useLazyGetUserInfoQuery, useLazyGetUserListQuery } from '../../redux/api/AccountApi'
import { GetUserListParam } from '../../redux/api/apiTypes'
import { format, fromUnixTime } from "date-fns";
import { useSelector } from 'react-redux';
import { useChangeFollowMutation } from '../../redux/api/FollowApi';

interface UserTable {
    key: number;
    name: string;
    email: string;
    phone: string;
    isFollow: number;
    username: string;
    userId: number;
}

const Follow = () => {
    const [form] = Form.useForm();
    const { Option } = Select;
    const [total, setTotal] = useState(0)
    const [userListTrigger, userListResponse] = useLazyGetUserListQuery()
    const [tableData, setTableData] = useState<UserTable[]>([])
    const [page, setPage] = useState(1);
    const pageSizeRef = useRef(10);
    const [submitValue, setSubmitValue] = useState<GetUserListParam>({})
    const [triggerCurrentUser, currentUser] = useLazyGetUserInfoQuery();

    const[changeFollow] = useChangeFollowMutation();
    useEffect(() => {
        triggerCurrentUser({});
    }, [triggerCurrentUser])
    useEffect(() => {
        userListTrigger({page: page-1, size: pageSizeRef.current});
    }, [userListTrigger])

    useEffect(() => {
        if (userListResponse.data?.content.length) {
            let newTableData: UserTable[] = []
            userListResponse.data.content.forEach(item => {
                if (item.userName == currentUser.data?.userName) {

                }
                else {
                    newTableData.push({
                        key: item.userId || 0,
                        name: item.fullName || '',
                        email: item.email || '',
                        phone: item.phoneNumber || '',
                        isFollow: item.isFollow || 0,
                        username: item.userName || '',
                        userId: item.userId || 0
                    })
                }
            })
            setTableData(newTableData)
            setTotal(userListResponse.data.totalElements)
        }
        else {
            setTableData([])
            setTotal(0)
        }
    }, [userListResponse])

    //get current user from redux
    const columns: ColumnsType<UserTable> = [
        {
            title: "STT",
            dataIndex: "stt",
            key: "stt",
            align: "center",
            render: (value, record, index) => (page - 1) * pageSizeRef.current + index + 1,
            width: '6%'
        },
        // {
        //     title: "X??? l??",
        //     dataIndex: "action",
        //     key: "action",
        //     align: "center",
        //     render: ((text, record) => (
        //         <Dropdown overlay={getContent(text, record)}>
        //             <SettingOutlined />
        //         </Dropdown>
        //     )
        //     ),
        //     width: '7%'

        // },
        {
            title: "H??? v?? t??n",
            dataIndex: "name",
            key: "name",
        },
        {
            title: "?????a ch??? email",
            dataIndex: "email",
            key: "email",
        },
        {
            title: "S??? ??i???n tho???i",
            dataIndex: "phone",
            key: "phone",
            width: '20%'
        },
        {
            title: "Tr???ng th??i",
            width: '10%',
            key: "isFollow",
            align: "center",
            render: ((text, record) => (
                <>

                    {record.isFollow === 1 ?
                        //follow button
                        <Button shape="round" size='large' onClick={() => followAction(record, 2)}>
                            Unfollow
                        </Button>
                        :
                        <Button type="primary" shape="round" size='large' onClick={() => followAction(record, 1)}>
                            Follow
                        </Button>
                    }

                </>
            )
            ),
        },
    ];
    const followAction = (iteam: UserTable, action: number) => {
        changeFollow({followId: iteam.userId, isFollow: action}).then(res => {
            if (res) {
                notification.success({
                    message: 'Th??nh c??ng',
                    description: 'Thay ?????i th??nh c??ng'
                })
                userListTrigger({page: page-1, size: pageSizeRef.current});
            } else {
                notification.error({
                    message: 'Th???t b???i',
                    description: 'Thay ?????i th???t b???i'
                })
            }
        })

    }

    



    const changePage = (currentPage: number, pageSize: number) => {
        const pageSizeChange = pageSizeRef.current !== pageSize;
        let newPage = currentPage;
        if (pageSizeChange) {
            newPage = 1;
        }
        setPage(newPage);
        pageSizeRef.current = pageSize;
        userListTrigger({ ...submitValue, page: newPage - 1, size: pageSize });
        console.log('submitvalue ', submitValue)
    }

    const triggerSearch = (values: GetUserListParam) => {
        setPage(1);
        userListTrigger({ ...values, page: 0, size: pageSizeRef.current, keyword: '' });
    }

    const onFinish = (values: any) => {
        let submitValues: GetUserListParam = {};
        if (values.fullName) {
            submitValues.fullName = values.fullName
        }
        if (values.phone) {
            submitValues.phoneNumber = values.phone;
        }
        if (values.email) {
            submitValues.email = values.email;
        }
        console.log('submitvalue 2', submitValue)
        setSubmitValue(submitValues);
        triggerSearch(submitValues);
    }

    return (
        <div className='test'>
            <Breadcrumb separator=">" style={{ marginLeft: 20, marginBottom: 20 }}>
                <Breadcrumb.Item href="">
                    <Link to='/home'>
                        Trang ch???
                    </Link>
                </Breadcrumb.Item>
                <Breadcrumb.Item>
                    Qu???n tr??? h??? th???ng
                </Breadcrumb.Item>
                <Breadcrumb.Item>Qu???n l?? ng?????i d??ng</Breadcrumb.Item>
            </Breadcrumb>
            <label className='title-label'>Danh s??ch ng?????i d??ng</label>

            <div className='content-box'>
                <label className='search-title'> T??m ki???m </label>
                <Form
                    name="searchBox"
                    onFinish={onFinish}
                    autoComplete="off"
                    layout="horizontal"
                    form={form}
                    labelWrap
                    labelAlign='left'
                    // className="custom-form"
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 116 }}
                    colon={false}
                >
                    <Row gutter={48}>
                        <Col span={12}>
                            {/* <Form.Item
                                label="H??? v?? t??n: "
                                name="name"
                            >
                                <Select
                                    // onChange={}
                                    placeholder="--L???a ch???n--"
                                >
                                    {userNamesList.map(item => (
                                        <Option value={item}> {item} </Option>
                                    ))}
                                </Select>
                            </Form.Item> */}
                            <Form.Item
                                label="H??? v?? t??n: "
                                name="fullName"
                            >
                                <Input placeholder="Nh???p t??n" />
                            </Form.Item>
                        </Col>

                        <Col span={12}>
                            <Form.Item
                                label="S??? ??i???n tho???i: "
                                name="phone"
                            >
                                <Input placeholder="Nh???p s??? ??i???n tho???i" />
                            </Form.Item>
                        </Col>
                    </Row>

                    <Row gutter={48}>
                        <Col span={12}>
                            <Form.Item
                                label="?????a ch??? email: "
                                name="email"
                            >
                                <Input placeholder="Nh???p ?????a ch??? email" />
                            </Form.Item>
                        </Col>

                        {/* <Col span={12}>
                            <Form.Item
                                label="Tr???ng th??i ho???t ?????ng: "
                                name="status"
                            >
                                <Select
                                    placeholder="--L???a ch???n--"
                                    allowClear
                                >
                                    <Option value="1">??ang ho???t ?????ng</Option>
                                    <Option value="3">???? kh??a</Option>
                                </Select>
                            </Form.Item>
                        </Col> */}
                    </Row>
                    <div className='search-box-button'>
                        <Button htmlType='button' onClick={() => form.resetFields()}>
                            X??a ??i???u ki???n
                        </Button>
                        <Button htmlType='submit' type='primary'>T??m ki???m</Button>
                    </div>
                </Form>
            </div>
            <div className='content-box'>
                <Row justify="space-between" align="middle">
                    <Col span={6}>
                        <div className='content-box-button'>
                            <div className='content-box-label' style={{ marginBottom: 20 }}>
                                <label>T???ng s??? b???n ghi: {total} </label>
                            </div>
                        </div>
                    </Col>

                </Row>

                <Table<UserTable>
                    dataSource={tableData}
                    columns={columns}
                    tableLayout="fixed"
                    pagination={false}
                />

                <Pagination
                    total={total}
                    defaultCurrent={1}
                    locale={{ items_per_page: ' d??ng' }}
                    pageSizeOptions={[10, 2, 20, 50, 100]}
                    defaultPageSize={pageSizeRef.current}
                    current={page}
                    showSizeChanger
                    onChange={changePage}
                />

                {/* <AddModal
                    setAddModal={setIsShown}
                    addModal={isShown}
                    isEdit={isEdit}
                    handleCancel={handleCancel}
                    openNotification={openNotification}
                    setIsEdit={setIsEdit}
                />
                <WarningModal
                    lockAcc={lockAcc}
                    unlockAcc={unlockAcc}
                    reset={reset}
                    accDelete={accDelete}
                    setLockAcc={setLockAcc}
                    setUnlockAcc={setUnlockAcc}
                    setReset={setReset}
                    setAccDelete={setAccDelete}
                    warningMessage={warningMessage}
                    openNotification={openNotification}
                    handleWarningCancel={handleWarningCancel}
                /> */}
            </div>
        </div>
    )
}

export default Follow