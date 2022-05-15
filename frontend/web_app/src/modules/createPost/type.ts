export interface CreatePostRequest {
    title: string,
    content: string,
}

export interface CreatePostResponse {
    id: number,
    title: string,
    content: string,
    isDelete: number,
}