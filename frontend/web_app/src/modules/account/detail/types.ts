import { User } from "../../../redux/api/apiTypes";

export type AccountDetailSubmit = Partial<User> & {
    provinceId?: number;
    districtId?: number;
    communeId?: number;
}