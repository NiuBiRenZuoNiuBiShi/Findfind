import {instance} from "./user";

export const getUserInfo = async (): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/userInfo', {
        headers: {
            Authorization: token
        }
    });
};
export const getUserInfoById = async (userId: any): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get(`/user/info/${userId}`, {
        headers: {
            Authorization: token
        }
    });
}
