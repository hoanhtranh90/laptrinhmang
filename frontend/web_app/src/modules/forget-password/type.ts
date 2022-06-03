export interface SendMailReq {
    email: string;
}

export interface ValidateOtpDTOAndChangePass {
    otp: number;
    email: string;
    newPassword: string;
    confirmPassword: string;
}