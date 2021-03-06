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
                                console.log("res1", res1);
                                
                                // let url = process.env.REACT_APP_API_URL + "/files/downloadFile/" + res1[0].attachmentId;
                                
                               

                                //string type
                                let imagePath: String[] = [];
                                res1.map((e,index) => {
                                    
                                    if(e.contentType.includes("audio")){
                                        item.imageFirst = process.env.REACT_APP_API_URL + "/files/downloadFile/" + e.attachmentId;
                                    }
                                    if(e.contentType.includes("image")){
                                        item.fileImage = process.env.REACT_APP_API_URL + "/files/downloadFile/" + e.attachmentId;; 
                                    }
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
    //l???y data l???n ?????u
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
                columnGutter={18} // Set kho???ng c??ch gi???a c??c column
                columnWidth={1450} // Set chi???u r???ng t???i thi???u l?? 300px
                overscanBy={1} // Gi?? tr??? ????? render tr?????c khi scroll t???i
                render={ImageCard} // Grid item c???a component

            ></Masonry>
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