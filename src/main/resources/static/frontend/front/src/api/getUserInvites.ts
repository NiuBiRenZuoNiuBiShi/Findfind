import {instance} from "./user";
export const getUserInvites = async (): Promise<any> =>
{
    const token = localStorage.getItem('token');
    return await instance.get('/user/invites', {
        headers: {
            Authorization: token
        }
    });
};
