import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { CommonListQuery, CreateProductDTO, ProductDetail, ProductResponseDTO, UpdateProductDTO } from './apiTypes';

export const marketApi = createApi({
    reducerPath: 'marketApi',
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_URL + "/market",
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem("accessToken");
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers;
        }
    }),
    endpoints: (builder) => ({
        //add
        addProduct: builder.mutation<ProductDetail, CreateProductDTO>({
            query: (body) => ({
                url: `add`,
                method: 'POST',
                body: body
            }),
            transformResponse: (response: { body: ProductDetail }, meta, arg) => {
                return response.body;
            }
        }),
        //search
        searchProduct: builder.query<ProductResponseDTO, Partial<CommonListQuery>>({
            query: (query) => ({
                url: `search?page=${query.page || 0}&properties=modifiedDate&size=${query.size || 100}&sortBy=DESC&keyword=${query.keyword || ''}`,
                method: 'POST',
                body: query
            }),
            transformResponse: (response: { body: ProductResponseDTO }, meta, arg) => {
                return response.body;
            }
        }),
        //update
        updateProduct: builder.mutation<ProductResponseDTO, UpdateProductDTO>({
            query: (body) => ({
                url: `update`,
                method: 'POST',
                body: body
            }),
            transformResponse: (response: { body: ProductResponseDTO }, meta, arg) => {
                return response.body;
            }
        }),
        //delete
        deleteProduct: builder.mutation<ProductResponseDTO, Number>({
            query: (id) => ({
                url: `delete/${id}`,
                method: 'DELETE'
            }),
            transformResponse: (response: { body: ProductResponseDTO }, meta, arg) => {
                return response.body;
            }
        }),
    })
})

export const { 
    useAddProductMutation,
    useLazySearchProductQuery,
    useUpdateProductMutation,
    useDeleteProductMutation
} = marketApi;
export default marketApi;