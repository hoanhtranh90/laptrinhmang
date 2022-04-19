import accountApi from '../../../../redux/api/AccountApi';
import { ChangePassword } from '../types';

export const ChangePasswordApi = accountApi.injectEndpoints({
    endpoints: (builder) => ({
        changePassword: builder.mutation<any, ChangePassword>({
            query: (body) => ({
                url: `change-pass`,
                method: 'POST',
                body
            }),
            transformResponse: (response: { body: any }, meta, arg) => {
                return response.body;
            },
        }),
    })
})

export const { useChangePasswordMutation } = ChangePasswordApi;