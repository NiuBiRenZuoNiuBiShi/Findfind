import { instance } from "./user";
export const getUserSeekers = async (): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/seekers', {
        headers: {
            Authorization: token
        }
    });
};