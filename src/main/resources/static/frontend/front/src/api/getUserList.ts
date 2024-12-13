import {instance} from "./user";
export const getUserList = async (): Promise<any> =>
{
    const token = localStorage.getItem('token');
    return await instance.get('/user/list', {
        headers: {
            Authorization: token
        }
    });
};
