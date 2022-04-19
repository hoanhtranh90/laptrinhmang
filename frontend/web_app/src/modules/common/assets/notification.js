import { notification } from "antd";
import { ReactComponent as IconSuccess } from '../img/icon/success.svg'
export const openNotification = (message) => {
    notification.open({
        icon: <IconSuccess />,
        message: message,
        // placement,
        // description: message,
        // duration: 5
    });
};