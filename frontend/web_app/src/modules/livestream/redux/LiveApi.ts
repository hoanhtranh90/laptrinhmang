import CreatePost from "..";
import homeApi from "../../../redux/api/HomeApi";


export const Live = homeApi.injectEndpoints({
    //live stream sse
    endpoints: builder => ({
        liveStream: builder.mutation<any, any>({
            query: () => ({
                method: "GET",
            headers: {
              Accept: "text/event-stream",
            },
                url: `sse/time`,
            }),
            transformResponse: (response: { body: any }, meta, arg) => {
                console.log(response);
                return response;
            }
        })
    })
})

export const {
    useLiveStreamMutation
    } = Live;