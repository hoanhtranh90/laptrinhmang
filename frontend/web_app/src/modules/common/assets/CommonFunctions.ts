import { compareAsc, getTime } from 'date-fns';
import jwtDecode from 'jwt-decode';

export const exportExcel = (url: string, filename: string) => {
    return new Promise((resolve, reject) => {
        fetch(url, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem("accessToken")
            }
        }).then(res => {
            return res.blob();
        }).then(blob => {
            const url = window.URL.createObjectURL(new Blob([blob]));
            const link = document.createElement('a');
            link.href = url;
            link.setAttribute('download', filename);
            link.click();
            resolve(true);
        })
            .catch(err => {
                reject(err.message);
            })
    })
}

type JWTDecode = {
    iss: string,
    iat: number,
    exp: number,
    ui: number,
    uname: string,
    rol: string,
    mail: string,
    fullname: string,
    ss: string,
}

export const isTokenExpired = (token: string) => {
    if (token) {
        let json: JWTDecode = jwtDecode(token);
        if (json.exp) {
            let diff = compareAsc(json.exp * 1000, getTime(new Date()));
            if (diff > 0) {
                return false;
            }
        }
    }
    return true;
}