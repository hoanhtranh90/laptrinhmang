import React, { useEffect } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { isTokenExpired } from "../modules/common/assets/CommonFunctions";
import { useLazyGetUserInfoQuery } from "../redux/api/AccountApi";
import { useAppDispatch, useAppSelector } from "../redux/Hooks";
import { setUser } from '../modules/login/redux/UserSlice';

function PrivateRoute({ children }: { children: JSX.Element }) {
    const isLogin = !isTokenExpired(localStorage.getItem("accessToken") || "")
    const user = useAppSelector((state) => state.user);
    const dispatch = useAppDispatch();

    let location = useLocation();

    const [trigger] = useLazyGetUserInfoQuery();

    useEffect(() => {
        if (!Object.keys(user.user).length && isLogin) {
            trigger({}).unwrap().then(res => {
                dispatch(setUser(res))
            })
        } else if (!isLogin) {
            dispatch(setUser({}))
        }
    }, [user, isLogin, trigger, dispatch])

    if (!isLogin) {
        return <Navigate to="/login" replace state={{ from: location }} />
    }

    return children;
}

export default PrivateRoute;
