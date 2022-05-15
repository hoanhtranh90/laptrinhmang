import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { CommonListQuery, GetUserListParam, User, UserListResponse } from './apiTypes';
import { Config } from '../../config/constant';

export const accountApi = createApi({
    reducerPath: 'accountApi',
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_URL + "/account-managers",
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem("accessToken");
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers;
        }
    }),
    endpoints: (builder) => ({
        getUserInfo: builder.query<User, any>({
            query: () => 'profile',
            transformResponse: (response: { body: User }, meta, arg) => {
                return response.body;
            },
        }),
        getUserList: builder.query<UserListResponse, Partial<CommonListQuery> & Partial<GetUserListParam>>({
            query: (body) => ({
                url: `search?page=${body.page || 0}&properties=userId&size=${body.size || 100}&sortBy=ASC`,
                method: 'POST',
                body: body,
            }),
            transformResponse: (response: { body: UserListResponse }, meta, arg) => {
                return response.body;
            },
        }),

    })
})

export const { useLazyGetUserInfoQuery, useLazyGetUserListQuery  } = accountApi;
export default accountApi;