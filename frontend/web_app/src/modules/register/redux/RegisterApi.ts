import { accountApi } from '../../../redux/api/AccountApi';
import { UserDTO } from '../../../redux/api/apiTypes';
import { UserRegisterDto } from '../type';

export const loginApi = accountApi.injectEndpoints({
    endpoints: (builder) => ({
        register: builder.mutation<UserDTO, UserRegisterDto>({
            query: (body) =>  ({
                url: 'register',
                method: 'POST',
                body: body
            }),
            transformResponse: (response: { body: UserDTO }, meta, arg) => {
                return response.body;
            }
        }),
        // getUserList: builder.query<UserListResponse, Partial<CommonListQuery> & Partial<GetUserListParam>>({
        //     query: (body) => ({
        //         url: `search?page=${body.page || 0}&properties=userId&size=${body.size || 100}&sortBy=ASC`,
        //         method: 'POST',
        //         body: body,
        //     }),
        //     transformResponse: (response: { body: UserListResponse }, meta, arg) => {
        //         return response.body;
        //     },
        // }),


    })
})

export const { useRegisterMutation } = loginApi;