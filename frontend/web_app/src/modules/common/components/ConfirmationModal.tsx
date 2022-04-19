import React from 'react'
import { Modal, Button } from 'antd'
import { ReactComponent as Warning } from '../../../assets/logos/warning.svg';

type ErrorProps = {
    visible: boolean;
    openNotification?: ()=> void;
    id?: number,
    cancelText: string,
    okText: string, 
    content: string,
    toggleEdit: () => void 
    toggleAddModal: () => void
    toggleConfirmModal: () => void
    setWarningModal: React.Dispatch<React.SetStateAction<boolean>>;

}

const Add = ({ visible, openNotification, id, cancelText, okText, content, toggleEdit, toggleAddModal, toggleConfirmModal, setWarningModal } : ErrorProps) => {
    const handleOk = () => {
        // toggleConfirmModal()
        if (openNotification) 
            openNotification()
        toggleEdit()
        toggleAddModal()
        setWarningModal(false)
    }

    const handleCancel = () => {
        // toggleConfirmModal()
        setWarningModal(false)
    }

    return (
        <>
            <Modal
                title={null}
                visible={visible}
                onCancel={handleCancel}
                centered
                footer={[
                    <>
                        <Button htmlType='button'
                            className='white-red-border-button'
                            onClick={() => handleCancel()}
                        >
                            {cancelText}
                        </Button>
                        <Button
                            htmlType='button'
                            className='red-button'
                            style={{ marginLeft: 20 }}
                            onClick={() => handleOk()}
                        >
                            {okText}
                        </Button>
                    </>
                ]}
                className='error-1'
            >
                 <Warning />
                <label>Xác nhận</label>
                <span>{content}</span>
            </Modal> </>
    )
}

export default Add