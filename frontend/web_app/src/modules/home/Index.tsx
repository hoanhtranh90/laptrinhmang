import './assets/css/index.css';
import { Button, Col, Row } from 'antd';
import React, { useEffect, useState, useRef } from 'react';
import { useLazyGetListPostQuery } from '../../redux/api/HomeApi';
import { useLazyGetFileByObjectIdAndTypeQuery } from '../../redux/api/FileApi';
import { fileTypes } from '../common/assets/ApiConst';
import { PostDetail, PostResponse } from '../../redux/api/apiTypes';
import _ from 'lodash';
import { Masonry } from "masonic";
import ImageCard from './ImageCrad';
import PostsSkeleton from './PostsSkeleton';
import CreatePost from '../createPost';
import { PlusOutlined } from '@ant-design/icons';

const Home = () => {
    const [trigger, response] = useLazyGetListPostQuery();
    const [fileIdTrigger] = useLazyGetFileByObjectIdAndTypeQuery();
    const [addVisible, setAddVisible] = useState(false);
    const [loading, setLoading] = useState(true);
    const pageSizeRef = useRef(10);
    const [listPost, setListPost] = useState<PostDetail[]>([]);
    let newListPost: React.SetStateAction<PostDetail[]> = [];
    const getData = () => {
        trigger({}).unwrap()
            .then(res => {
                const addFieldToPost = () => {
                    newListPost = _.cloneDeep(res.content);
                    const promises = newListPost.map(async (item, index) => {
                        await fileIdTrigger({ objectId: item.id, objectType: fileTypes.POST }).unwrap()
                            .then(res1 => {
                                let url = process.env.REACT_APP_API_URL + "/files/downloadFile/" + res1[0].attachmentId;
                                item.imageFirst = url;

                                //string type
                                let imagePath: String[] = [];
                                res1.map(e => {
                                    let url = process.env.REACT_APP_API_URL + "/files/downloadFile/" + e.attachmentId;
                                    imagePath.push(url);
                                })
                                item.imagePath = imagePath;
                                console.log("3");

                            })
                            .catch((err) => {
                                console.error(err);
                            })
                    })
                    return Promise.all(promises);
                }
                addFieldToPost().then(() => {
                    setListPost(newListPost);
                    setLoading(false);
                })
            })
            .then(() => {

            })
    }
    //lấy data lần đầu
    useEffect(() => {
        getData();

    }, [trigger, fileIdTrigger,addVisible]);
    

    const toggleAddModal = () => {
        if (addVisible) {
            triggerSearch();
        }
        setAddVisible(!addVisible);
    }
    const triggerSearch = () => {
        // setPage(1);
        trigger({});
    }

    
    if (loading) {
        return <PostsSkeleton />
    }
    return (
        <div className='home'>
            <Masonry
                items={listPost}
                columnGutter={18} // Set khoảng cách giữa các column
                columnWidth={450} // Set chiều rộng tối thiểu là 300px
                overscanBy={5} // Giá trị để render trước khi scroll tới
                render={ImageCard} // Grid item của component

            ></Masonry>
            {/* <Row justify="space-between" align="middle">
                <Col span={6}>
                    <div className='content-box-button'>
                        <div className='content-box-label' style={{ marginBottom: 20 }}>
                            <label style={{ fontWeight: 700 }}>Tổng số 0 bản ghi </label>
                        </div>
                    </div>
                </Col>
                <Col span={6} style={{ textAlign: 'right' }}>
                    <Button type='primary' style={{ marginLeft: 0 }} onClick={toggleAddModal}>Thêm mới</Button>
                </Col>
            </Row> */}
            {/* floating button */}
            <div className='floating-button'>
                
                <Button type='primary' icon={<PlusOutlined />} onClick={toggleAddModal} className="floating-icon">
                    
                </Button>
            </div>
            
            {addVisible ?
                <CreatePost visible={addVisible} toggleModal={toggleAddModal} onSuccess={() => triggerSearch()} />
                : <></>}
        </div>
    )
}

export default Home;