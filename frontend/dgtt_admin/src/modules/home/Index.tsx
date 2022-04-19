import './assets/css/index.css';
import { Col, Row } from 'antd';
import React, { useEffect, useState } from 'react';
import { useLazyGetListPostQuery } from '../../redux/api/HomeApi';
import { useLazyGetFileByObjectIdAndTypeQuery } from '../../redux/api/FileApi';
import { fileTypes } from '../common/assets/ApiConst';
import { PostDetail, PostResponse } from '../../redux/api/apiTypes';
import _ from 'lodash';
import { Masonry } from "masonic";
import ImageCard from './ImageCrad';
import PostsSkeleton from './PostsSkeleton';

const Home = () => {
    const [trigger] = useLazyGetListPostQuery();
    const [fileIdTrigger] = useLazyGetFileByObjectIdAndTypeQuery();
    const [loading, setLoading] = useState(true);
    const [listPost, setListPost] = useState<PostDetail[]>([]);
    let newListPost: React.SetStateAction<PostDetail[]> = [];
    useEffect(() => {
        trigger({}).unwrap()
            .then( res => {
                const addFieldToPost = () => {
                    newListPost =  _.cloneDeep(res.content);
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
                console.log("1");      
            })
            .then(() => {
               
            })
       
    }, [trigger, fileIdTrigger])


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
        </div>
    )
}

export default Home;