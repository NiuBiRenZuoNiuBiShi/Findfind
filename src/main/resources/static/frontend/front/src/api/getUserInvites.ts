import { instance } from "./user";

export const getUserInvites = async (page: number, size: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/invites', {
        params: {
            page: page,
            size: size
        },
        headers: {
            Authorization: token
        }
    });
};