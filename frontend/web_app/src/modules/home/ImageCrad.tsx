import React, { useRef, useState } from 'react'
import { CommentResponse, PostDetail } from '../../redux/api/apiTypes';
import './assets/css/index.css';
import { Avatar, Image, Card, Modal, Carousel, Typography, Badge, Divider, Input, Button } from 'antd';
import { HeartFilled, HeartOutlined, MessageOutlined, ShareAltOutlined } from '@ant-design/icons';
import { useAddCommentMutation, useLazyGetListCommentQuery } from '../../redux/api/HomeApi';
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
    const [addComment] = useAddCommentMutation();

    //set type any
    const slider = useRef<any>(null);

    const handleSubmitCmt = (e:any) => {
        e.preventDefault();
        if (comment.trim() === "") {
            setErrors("Write something");
            setTimeout(() => {
                setErrors("");
            }, 2000);
        } else {
            const newComment = { body: comment };
            addComment({content: comment, postId: props.data.id}).unwrap()
            .then( res => {
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

    const SampleNextArrow = (props:any) => {
        const { className, style, onClick } = props
        return (
          <div
            className={className}
            style={{
              ...style,
              color: 'black',
              fontSize: '15px',
              lineHeight: '1.5715',
              backgroundColor: "#ccc",
              opacity: ".7",
              justifyContent: 'center',
              display: "flex",
              width: "2.5rem",
              height: "2.5rem",
              borderRadius: "50%",
              zIndex: "100"
    
            }}
            onClick={onClick}
          >
          </div>
        )
      }
      const SamplePrevArrow = (props:any) => {
        const { className, style, onClick } = props
        return (
          <div
            className={className}
            style={{
              ...style,
              color: 'black',
              fontSize: '15px',
              lineHeight: '1.5715',
              backgroundColor: "#ccc",
              opacity: ".7",
              justifyContent: 'center',
              display: "flex",
              width: "2.5rem",
              height: "2.5rem",
              borderRadius: "50%",
              zIndex: "100"
    
            }}
            onClick={onClick}
          >
          </div>
        )
      }
      const settings = {
        nextArrow: <SampleNextArrow onClick={() => slider.current.prev()} />,
        prevArrow: <SamplePrevArrow onClick={() => slider.current.next()} />
      }
    const showModal = () => {
        getComment(props.data.id).unwrap()
        .then((res) => {
            console.log("res", res);
            setListComment(res)
        })


        // setVisible(true);
        setIsModalVisible(true);
    };

    const handleOk = () => {
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    console.log('ImageCard:', props);

    return (
        <div  >
             <div className={'card'} onClick={showModal} onMouseOver={()=>setCheckHover(true)} onMouseLeave={()=>setCheckHover(false)}>
                <img src={props.data.imageFirst} alt='image' className={'img'} />
                <Meta className="mt-1"
                    style={!checkHover ? { marginRight: "auto", padding: " 0 15px", opacity: 0, maxHeight: 0, transition: "all 0.3s ease-in-out" } : { maxHeight: "100%", marginRight: "auto", padding: "15px", opacity: 1, transition: "all 0.3s ease-in-out" }}
                    avatar={<Avatar style={{ backgroundColor: "#000" }}>{props.data.createdBy && props.data.createdBy.charAt(0).toUpperCase()}</Avatar>}
                    title={props.data.title}
                />
            </div>
            <Modal title={props.data.title} visible={isModalVisible} onOk={handleOk} onCancel={handleCancel} footer={[]} width={"90%"} centered bodyStyle={{ backgroundColor: "#fff" }} >
                <div style={{ display: "flex", justifyContent: "space-between" }}>
                    <div style={{ width: "80%" }}>
                        <Carousel dots={false} ref={slider} arrows {...settings}>
                            {props.data && props.data.imagePath.map((item:string, index: number) => {
                                return <div style={{ display: "flex" }}>
                                    <Image
                                        preview={false}
                                        width={"100%"}
                                        height={"100%"}
                                        fallback="data:image/png;base64,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"
                                        src= { props.data ? (item) : ""}
                                        alt="Không kèm hình ảnh"


                                    />
                                </div>
                            })}
                        </Carousel>


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
                            <ShareAltOutlined style={{ fontSize: "20px" }} />

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