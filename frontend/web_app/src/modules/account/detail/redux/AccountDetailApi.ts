import accountApi from '../../../../redux/api/AccountApi';
import { User } from '../../../../redux/api/apiTypes';
import { AccountDetailSubmit } from '../types';

export const UpdateDetailApi = accountApi.injectEndpoints({
    endpoints: (builder) => ({
        updateDetail: builder.mutation<User, AccountDetailSubmit>({
            query: (body) => ({
                url: `update-profile`,
                method: 'PUT',
                body
            }),
            transformResponse: (response: { body: User }, meta, arg) => {
                return response.body;
            },
        }),
    })
})

export const { useUpdateDetailMutation } = UpdateDetailApi;