import { accountApi } from '../../../redux/api/AccountApi';

export const loginApi = accountApi.injectEndpoints({
    endpoints: (builder) => ({
        login: builder.mutation({
            query: (credentials) => ({
                url: 'login',
                method: 'POST',
                body: credentials
            })
        }),

    })
})

export const { useLoginMutation } = loginApi;