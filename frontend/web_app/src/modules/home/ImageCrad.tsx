import React, { useRef, useState } from 'react'
import { CommentResponse, PostDetail } from '../../redux/api/apiTypes';
import './assets/css/index.css';
import TimeSlider from "react-input-slider";
import { Avatar, Image, Card, Modal, Carousel, Typography, Badge, Divider, Input, Button, message } from 'antd';
import { CaretRightOutlined, DeleteOutlined, HeartFilled, HeartOutlined, MessageOutlined, PauseOutlined, ShareAltOutlined, StepBackwardOutlined, StepForwardOutlined } from '@ant-design/icons';
import { useAddCommentMutation, useDeletePostMutation, useLazyGetListCommentQuery } from '../../redux/api/HomeApi';
import { AudioPlayer } from './component/AudioPlayer';
const { Meta } = Card;
const ImageCard = (props: any) => {
    const [visible, setVisible] = useState(false);
    const [comment, setComment] = useState("");
    const [loading, setLoading] = useState(false);
    const [errors, setErrors] = useState("");
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [checkHover, setCheckHover] = useState(false);

    const [listComment, setListComment] = useState<CommentResponse[]>([]);
    const [getComment] = useLazyGetListCommentQuery();
    const [deletePostEx] = useDeletePostMutation();
    const [addComment] = useAddCommentMutation();


    const [isPlay, setPlay] = useState(false);
    const childRef = useRef();
    //set type any
    const slider = useRef<any>(null);

    const handleSubmitCmt = (e: any) => {
        e.preventDefault();
        if (comment.trim() === "") {
            setErrors("Write something");
            setTimeout(() => {
                setErrors("");
            }, 2000);
        } else {
            const newComment = { body: comment };
            addComment({ content: comment, postId: props.data.id }).unwrap()
                .then(res => {
                    console.log("res", res);

                    setListComment([...listComment, res])
                })
                .catch(err => {

                })
            setLoading(true);
            setTimeout(() => {
                setComment("");
                setLoading(false);
            }, 200);
        }
    };


    const showModal = () => {
        getComment(props.data.id).unwrap()
            .then((res) => {
                console.log("res", res);
                setListComment(res)
            })


        // setVisible(true);
        setIsModalVisible(true);
    };
    const deletePost = () => {
        console.log(props);
        let postId = props.data.id;
        deletePostEx({ postId }).unwrap()
            .then(res => {
                console.log("res", res);
                //message
                message.success("Thành công");
                window.location.reload();
            })
        //reload page
        // 
    }

    const handleOk = () => {
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
        // @ts-ignore
        childRef.current.pause();
        //remove this modal
        // document.getElementsByClassName("audioPlayer")[0].remove();

    };

    console.log('ImageCard:', props);

    return (
        <div  >
            <div className={'card'} onClick={showModal} onMouseOver={() => setCheckHover(true)} onMouseLeave={() => setCheckHover(false)}>
                {/* <img src={props.data.imageFirst} alt='image' className={'img'} /> */}
                <Meta className="mt-1"
                    style={{ maxHeight: "100%", marginRight: "auto", padding: "15px", opacity: 1, transition: "all 0.3s ease-in-out" }}
                    avatar={<Avatar style={{ backgroundColor: "#000" }}>{props.data.createdBy && props.data.createdBy.charAt(0).toUpperCase()}</Avatar>}
                    title={props.data.title}
                />
            </div>
            <Modal title={props.data.title} visible={isModalVisible} onOk={handleOk} onCancel={handleCancel} footer={[]} width={"90%"} centered bodyStyle={{ backgroundColor: "#fff" }} >
                <div style={{ display: "flex", justifyContent: "space-between" }}>
                    <div style={{ width: "80%" }}>
                       

                        <div className="App">
                            <img className="Song-Thumbnail" src={props.data.fileImage} alt="tet" style={{maxWidth:"500px"}}/>
                            <h2 className="Song-Title">{props.data.title}</h2>
                            <p className="Singer">{props.data.createdBy}</p>
        
                    


                            {/* @ts-ignore */}
                            <AudioPlayer ref={childRef} audio = {props.data.imageFirst}/>

                        </div>
                    </div>
                    <div style={{ width: "20%", marginLeft: "20px" }}>
                        <Meta className="mt-1"
                            avatar={<Avatar style={{ backgroundColor: "#000" }}>{props.data.createdBy.charAt(0).toUpperCase()}</Avatar>}
                            title={props.data.createdBy}
                        />
                        <div style={{ marginTop: "20px" }}>
                            <Typography.Text strong>{props.data.title}</Typography.Text>
                            <br />
                            <Typography.Text>{props.data.content}</Typography.Text>
                            <hr />
                        </div>
                        <div className="actions" style={{ marginTop: "20px" }}>
                            <div className="d-flex">
                                {true === true ? (
                                    <Badge count={props.data.likeCount}>
                                        <HeartFilled
                                            disabled={true}
                                            style={{ fontSize: "20px" }}
                                        />
                                    </Badge>
                                ) : (
                                    <Badge showZero count={props.data.likeCount}>
                                        <HeartOutlined
                                            style={{ fontSize: "20px" }}
                                        />
                                    </Badge>
                                )}
                            </div>

                            <div
                                className="d-flex"
                            >
                                <Badge showZero count={props.data.commentCount}>
                                    <MessageOutlined style={{ fontSize: "20px" }} />
                                </Badge>
                            </div>
                            <DeleteOutlined onClick={deletePost} style={{ fontSize: "20px" }} />

                        </div>
                        <hr />

                        <div style={{ marginTop: "20px" }}>
                            <div className="comment-form">
                                <Divider />
                                <div className="d-flex">
                                    <Input
                                        onChange={(e) => setComment(e.target.value)}
                                        size="small"
                                        allowClear
                                        placeholder="type comment"
                                        value={comment}
                                        onPressEnter={handleSubmitCmt}
                                    />
                                    <Button disabled={loading} onClick={handleSubmitCmt}>
                                        Submit
                                    </Button>
                                </div>
                                {errors && <span className="text-danger">{errors}</span>}
                            </div>
                        </div>
                        <div style={{ marginTop: "20px", height: "50vh", overflow: 'scroll' }}>
                            {listComment && listComment.map(cmt => {
                                return <Meta className="mt-1"
                                    avatar={<Avatar style={{ backgroundColor: "#000" }}>{cmt.user.fullName.charAt(0).toUpperCase()}</Avatar>}
                                    title={cmt.user.fullName}
                                    description={cmt.body}
                                />
                            })}
                        </div>
                    </div>
                </div>
            </Modal>
        </div>
    )
}
export default ImageCard;