import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { Config } from '../../config/constant';
import { CommonListQuery, PostResponse, CommentResponse, CommentRequest } from './apiTypes';

const homeApi = createApi({
    reducerPath: 'homeApi',
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_URL + '/',
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem("accessToken");
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers;
        }
    }),
    endpoints: (builder) => ({
        getListPost: builder.query<PostResponse, Partial<CommonListQuery>>({
            query: (query) => ({
                url: `post/search?page=${query.page || 0}&properties=modifiedDate&size=${query.size || 10}&sortBy=ASC`,
                method: 'POST',
                body: query
            }),
            transformResponse: (response: { body: PostResponse }, meta, arg) => {
                console.log(response);
                
                return response.body;
            },
        }),
        getListComment: builder.query<CommentResponse[], Number>({
            query: (query) => ({
                url: `comment/get/${query}`,
                method: 'GET'
            }),
            transformResponse: (response: { body: CommentResponse[] }, meta, arg) => {
                return response.body;
            },
        }),
        addComment: builder.mutation<CommentResponse, CommentRequest>({
            query: (body) => ({
                url: `comment/add`,
                method: 'POST',
                body: body
            }),
            transformResponse: (response: { body: CommentResponse }, meta, arg) => {
                console.log(response);
                
                return response.body;
            },
        }),
    })
})

export default homeApi;
export const {
  useLazyGetListPostQuery,
  useAddCommentMutation,
  useLazyGetListCommentQuery
} = homeApi;