import { instance } from "./user";

export const getUserJoins = async (page: number, size: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/joins', {
        params: {
            page: page,
            size: size
        },
        headers: {
            Authorization: token
        }
    });
};