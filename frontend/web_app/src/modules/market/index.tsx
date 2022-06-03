import { PlusOutlined } from "@ant-design/icons";
import { Button } from "antd";
import _ from "lodash";
import { Masonry } from "masonic";
import { useEffect, useState } from "react";
import { ProductDetail, ProductResponseDTO } from "../../redux/api/apiTypes";
import { useLazyGetFileByObjectIdAndTypeQuery } from "../../redux/api/FileApi";
import { useLazySearchProductQuery } from "../../redux/api/MarketApi";
import { fileTypes } from "../common/assets/ApiConst";
import CreatePost1 from "./component/CreatePost1";
import ImageCard from "./component/ImageCrad";

const Market = () => {
    const [trigger, response] = useLazySearchProductQuery();
    const [fileIdTrigger] = useLazyGetFileByObjectIdAndTypeQuery();
    const [addVisible, setAddVisible] = useState(false);
    const [loading, setLoading] = useState(true);

    const [listPost, setListPost] = useState<ProductDetail[]>([]);
    let newListPost: React.SetStateAction<ProductDetail[]> = [];
    const getData = () => {
        trigger({}).unwrap()
            .then(res => {
                const addFieldToPost = () => {
                    newListPost = _.cloneDeep(res.content);
                    const promises = newListPost.map(async (item, index) => {
                        await fileIdTrigger({ objectId: item.productId, objectType: fileTypes.MARKET }).unwrap()
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
    return (
        <div>
            <Masonry
                items={listPost}
                columnGutter={18} // Set khoảng cách giữa các column
                columnWidth={300} // Set chiều rộng tối thiểu là 300px
                overscanBy={5} // Giá trị để render trước khi scroll tới
                render={ImageCard} // Grid item của component

            ></Masonry>
            <div className='floating-button'>
                
                <Button type='primary' icon={<PlusOutlined />} onClick={toggleAddModal} className="floating-icon">
                    
                </Button>
            </div>
            {addVisible ?
                <CreatePost1 visible={addVisible} toggleModal={toggleAddModal} onSuccess={() => triggerSearch()} />
                : <></>}
        </div>
    )
}
export default Market;