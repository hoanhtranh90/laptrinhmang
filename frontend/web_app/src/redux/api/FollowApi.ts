import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { ChangeFollowRequest, CommonListQuery, GetUserListParam, User, UserDTO, UserListResponse } from './apiTypes';
import { Config } from '../../config/constant';

export const followApi = createApi({
    reducerPath: 'followApi',
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_URL + "/follow",
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem("accessToken");
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers;
        }
    }),
    endpoints: (builder) => ({
        changeFollow: builder.mutation<UserDTO, ChangeFollowRequest>({
            query: (body) => ({
                url: `follow`,
                method: 'POST',
                body: body,
            }),
            transformResponse: (response: { body: UserDTO }, meta, arg) => {
                return response.body;
            }
        }),
    })
})

export const { useChangeFollowMutation } = followApi;
export default followApi;