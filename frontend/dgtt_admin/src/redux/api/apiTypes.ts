

export interface CommonListQuery {
    keyword?: string,
    page?: number,
    size?: number,
    searchStatusDTO?: StatuteParam,
}


export interface UploadFileType {
    file: FormData,
    note?: string,
    objectType: number,
    storageType?: string,
}

export interface UploadFileForBidOrderType {
    listFileIds: number[],
    objectId: number,
    objectType: number,
}

export interface UploadFileResponse {
    attachmentId: number,
    contentType: string,
    creatorId: number,
    fileExtention: string,
    fileName: string,
    fileServiceId: number,
    isDelete: number,
    isEncrypt: number,
    note: string,
    objectType: number,
    pdfAlready: number,
    storageType: string,
    updatorId: number,
}

export interface FileByObjectIdAndType {
    objectId: number,
    objectType: number,
}






export interface User {
    address?: string,
    createdBy?: string,
    createdDate?: number,
    dateOfBirth?: number,
    email?: string,
    fullName?: string,
    modifiedBy?: string,
    modifiedDate?: number,
    phoneNumber?: string,
    roleCodes?: string[],
    sex?: number,
    status?: number,
    statusStr?: string,
    userId?: number,
    userName?: string,
    identificationNumber?: string,
}



export interface StatuteParam {
    auctionMethodId?: number,
    auctionTypeId?: number,
    fromDate?: string,
    isPublic?: number,
    operatorId?: number,
    productName?: string,
    publicProgressAuction?: number,
    status?: number,
    statuteCode?: string,
    toDate?: string,
    type?: number
}


export interface PostResponse {
    content: PostDetail[],
    totalElements: number,
}
//CommentResponse
export interface CommentResponse {
    
}
export interface PostDetail {
    id: number,
    title?: string,
    content?: string,
    createdBy?: string,
    createdDate?: number,
    modifiedBy?: string,
    modifiedDate?: number,
    isDelete?: number,
    [key: string]: any
}