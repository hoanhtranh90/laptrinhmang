import React from 'react'
import { Modal, Button } from 'antd'
import { ReactComponent as Warning } from '../../../assets/logos/warning.svg';

type ConfirmationProps = {
    visible: boolean;
    toggleModal: () => void;
    message: string;
    modalAction: () => void;
}

const ConfirmationModal = ({ visible, toggleModal, message, modalAction }: ConfirmationProps) => {
    return (
        <>
            <Modal
                title={null}
                visible={visible}
                onCancel={toggleModal}
                footer={[
                    <React.Fragment key="footer">
                        <Button htmlType='button'
                            className='white-red-border-button'
                            onClick={toggleModal}
                        >
                            Hủy bỏ
                        </Button>
                        <Button
                            htmlType='button'
                            type='primary'
                            style={{ marginLeft: 20 }}
                            onClick={modalAction}
                        >
                            Xác nhận
                        </Button>
                    </React.Fragment>
                ]}
                className='error-1'
            >
                <Warning />
                <label>Xác nhận</label>
                <p>{message}</p>
            </Modal> </>
    )
}

export default ConfirmationModal