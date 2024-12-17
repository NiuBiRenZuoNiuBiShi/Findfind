import { instance } from "./user";

export const getUserList = async (page: number, size: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/list', {
        params: {
            page: page,
            size: size
        },
        headers: {
            Authorization: token
        }
    });
};