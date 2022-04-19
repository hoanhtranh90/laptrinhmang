import React from 'react'
import { Modal, Button } from 'antd'
import { ReactComponent as Warning } from '../../../assets/logos/warning.svg';

type ErrorProps = {
    type: number,
    widthConfig: number,
    visible: boolean;
    cancelText: string,
    okText: string,
    classBtnOk: string,
    contents: string[],
    handleOk: () => void,
    handleCancel: () => void,
    okBtnDanger?: boolean,
}

const ConfirmModal = ({ type, widthConfig, visible, cancelText, okText, classBtnOk, contents, handleOk, handleCancel, okBtnDanger }: ErrorProps) => {
    return (
        <>
            <Modal
                width={widthConfig || 424}
                title={null}
                visible={visible}
                onCancel={handleCancel}
                centered
                footer={[
                    <React.Fragment key="footer">
                        <Button htmlType='button'
                            className='white-red-border-button'
                            onClick={() => handleCancel()}
                        >
                            {cancelText}
                        </Button>
                        <Button
                            danger={okBtnDanger}
                            htmlType='button'
                            className={classBtnOk}
                            style={{ marginLeft: 20 }}
                            onClick={() => handleOk()}
                            type="primary"
                        >
                            {okText}
                        </Button>
                    </React.Fragment>
                ]}
                className='error-1'
            >
                {type === 1 ? <Warning /> : <></>}
                <label>Xác nhận</label>
                {contents && contents.length ? contents.map((item, index) => <div key={index}>{item}</div>) : <></>}
            </Modal> </>
    )
}

export default ConfirmModal