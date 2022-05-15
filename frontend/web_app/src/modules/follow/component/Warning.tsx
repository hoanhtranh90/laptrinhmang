import React from 'react'
import { Modal, Button } from 'antd'
import { ReactComponent as Warning } from '../../../../assets/logos/warning.svg';

type WarningProps = {
    lockAcc: boolean;
    unlockAcc: boolean;
    reset: boolean;
    accDelete: boolean;
    setLockAcc: React.Dispatch<React.SetStateAction<boolean>>;
    setUnlockAcc: React.Dispatch<React.SetStateAction<boolean>>;
    setReset: React.Dispatch<React.SetStateAction<boolean>>;
    setAccDelete: React.Dispatch<React.SetStateAction<boolean>>;
    warningMessage: () => string;
    handleWarningCancel: () => void;
    openNotification: () => void;
}

const OpWarning = ({ lockAcc, unlockAcc, reset, accDelete, handleWarningCancel, openNotification, warningMessage, setAccDelete, setLockAcc, setReset, setUnlockAcc }: WarningProps) => {
    const handleOk = () => {
        openNotification()
        if (lockAcc) setLockAcc(false)
        if (unlockAcc) setUnlockAcc(false)
        if (reset) setReset(false)
        if (accDelete) setAccDelete(false)
    }

    return (
        <>
            <Modal
                title={null}
                visible={lockAcc || unlockAcc || reset || accDelete}
                onCancel={handleWarningCancel}
                footer={[
                    <>
                        <Button htmlType='button'
                            className='white-red-border-button'
                            onClick={handleWarningCancel}
                        >
                            Hủy bỏ
                        </Button>
                        <Button
                            htmlType='button'
                            className='red-button'
                            style={{ marginLeft: 20 }}
                            onClick={handleOk}
                        >
                            Đồng ý
                        </Button>
                    </>
                ]}
                className='error-1'
            >
                <Warning />
                <label>Xác nhận</label>
                <p>{warningMessage()}</p>
            </Modal> </>
    )
}

export default OpWarning