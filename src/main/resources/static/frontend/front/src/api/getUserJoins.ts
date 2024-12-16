import {instance} from "./user";
export const getUserJoins = async (): Promise<any> =>
{
    const token = localStorage.getItem('token');
    return await instance.get('/user/joins', {
        headers: {
            Authorization: token
        }
    });
};
