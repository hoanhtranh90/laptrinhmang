import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { UploadFileType, UploadFileForBidOrderType, FileByObjectIdAndType, UploadFileResponse } from './apiTypes';
import { Config } from '../../config/constant';

const fileApi = createApi({
    reducerPath: 'fileApi',
    baseQuery: fetchBaseQuery({
        baseUrl: process.env.REACT_APP_API_URL + '/files',
        prepareHeaders: (headers, { getState }) => {
            const token = localStorage.getItem("accessToken");
            if (token) {
                headers.set('authorization', `Bearer ${token}`)
            }
            return headers;
        }
    }),
    endpoints: (builder) => ({
        // Upload file 
        uploadFile: builder.mutation<UploadFileResponse, UploadFileType>({
            query: (body) => ({
                url: `uploadFile?objectType=${body.objectType}&note=${body.note || ""}&storageType=${body.storageType}`,
                method: 'POST',
                body: body.file
            }),
            transformResponse: (response: { body: UploadFileResponse }, meta, arg) => {
                return response.body;
            },
        }),
        // Map file with object: stutue, contract,...
        uploadFileForObject: builder.mutation<number, UploadFileForBidOrderType>({
            query: (body) => ({
                url: `upload`,
                method: 'PUT',
                body: body
            }),
        }),
        // Get file by object
        getFileByObjectIdAndType: builder.query<UploadFileResponse[], FileByObjectIdAndType>({
            query: (query) => `getFileByObjectIdAndObjectType?objectId=${query.objectId}&objectType=${query.objectType}`,
            transformResponse: (response: { body: UploadFileResponse[] }, meta, arg) => {
                return response.body;
            },
        }),
        // Delete file
        deleteFile: builder.mutation<File, number>({
            query: (id) => ({
                url: `deleteFile/${id}`,
                method: 'DELETE'
            }),
            transformResponse: (response: { body: File }, meta, arg) => {
                return response.body;
            },
        }),
        // Download file
        downloadFileBase64: builder.query<any, number>({
            query: (id) => `downloadFileBase64/${id}`,
            transformResponse: (response: { body: any }, meta, arg) => {
                return response.body;
            },
        }),
    })
})

export default fileApi;
export const {
    useUploadFileMutation,
    useUploadFileForObjectMutation,
    useLazyGetFileByObjectIdAndTypeQuery,
    useDeleteFileMutation,
    useLazyDownloadFileBase64Query
} = fileApi;