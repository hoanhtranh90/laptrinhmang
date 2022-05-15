import CreatePost from "..";
import homeApi from "../../../redux/api/HomeApi";
import { CreatePostRequest, CreatePostResponse } from "../type";


export const CreatePostApi = homeApi.injectEndpoints({
    endpoints: builder => ({
        addPost: builder.mutation<CreatePostResponse, CreatePostRequest >({
            query: (post) => ({
                url: `post/create`,
                method: 'POST',
                body: post

            }),
            transformResponse: (response: { body: CreatePostResponse }, meta, arg) => {
                console.log(response);
                return response.body;
            }


        })
    })
})

export const {
    useAddPostMutation
} = CreatePostApi;