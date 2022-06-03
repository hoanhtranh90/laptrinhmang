import { accountApi } from '../../../redux/api/AccountApi';
import { UserDTO } from '../../../redux/api/apiTypes';
import { SendMailReq, ValidateOtpDTOAndChangePass } from '../type';

export const forgetPass = accountApi.injectEndpoints({
    endpoints: (builder) => ({
        genOTP: builder.mutation<any, Partial<SendMailReq>>({
            query: (body) =>  ({
                url: `generateOtp/${body.email}`,
                method: 'GET'
                        }),
            transformResponse: (response: { body: any }, meta, arg) => {
                return response.body;
            }
        }),
        validateOtpAndChangePass: builder.mutation<String, ValidateOtpDTOAndChangePass>({
            query: (body) =>  ({
                url: `validateOtpAndChangePass`,
                method: 'POST',
                body: body
            }),
            transformResponse: (response: { body: String }, meta, arg) => {
                return response.body;
            }
        })
        
    })
})

export const { useGenOTPMutation, useValidateOtpAndChangePassMutation } = forgetPass;