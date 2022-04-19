import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { User } from './apiTypes';
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

    })
})

export const { useLazyGetUserInfoQuery } = accountApi;
export default accountApi;