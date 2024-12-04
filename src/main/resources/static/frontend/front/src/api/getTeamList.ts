import {instance} from "./team";

export const getTeamList = async (): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/teams/list', {
        headers: {
            Authorization: token
        }
    });
};
