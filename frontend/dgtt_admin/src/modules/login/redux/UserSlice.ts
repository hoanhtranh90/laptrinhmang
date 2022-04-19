import { createSlice } from "@reduxjs/toolkit";
import { loginApi } from "./LoginApi";
import { User } from '../../../redux/api/apiTypes';

interface UserState {
    userToken: string,
    isLoading: boolean,
    errorMessage: string,
    user: User,
}

const initialState: UserState = {
    userToken: '',
    isLoading: false,
    errorMessage: '',
    user: {},
}

export const userSlice = createSlice({
    name: 'user',
    initialState,
    reducers: {
        logout: (state) => {
            state.userToken = '';
            state.errorMessage = '';
            localStorage.removeItem("accessToken");
        },
        setToken: (state, action) => {
            state.userToken = action.payload.accessToken;
        },
        setUser: (state, action) => {
            state.user = action.payload;
        }
    },
    extraReducers: (builder) => {
        builder
            .addMatcher(loginApi.endpoints.login.matchFulfilled, (state, action) => {
                state.userToken = action.payload?.body?.access_token;
                state.errorMessage = '';
            })
            .addMatcher(loginApi.endpoints.login.matchRejected, (state, action) => {
                state.userToken = '';
                let data = action.payload?.data as any;
                state.errorMessage = data?.message;
            })
    }
})

export const { logout, setToken, setUser } = userSlice.actions;
export default userSlice.reducer;