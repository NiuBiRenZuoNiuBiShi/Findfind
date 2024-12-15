import {instance} from "./user";
export const userUpdate = async (user): Promise<any> =>
{
    const token = localStorage.getItem('token');
    return await instance.post('/user/update',user, {
        headers: {
            Authorization: token
        }
    });
};
