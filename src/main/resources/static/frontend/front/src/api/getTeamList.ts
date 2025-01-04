import {instance} from "./team";

export const getTeamList = async (): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/teams/list', {
        headers: {
            Authorization: token
        }
    });
};
export const getTeamFromUser = async (): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/teams/getByUser', {
        headers: {
            Authorization: token
        }
    });
};
export const getTeamById = async (id): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get(`/teams/get/${id}`, {
        headers: {
            Authorization: token
        }
    });
};
export const getTeamUsers = async (id): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get(`/teams/listUsers/${id}`, {
        headers: {
            Authorization: token
        }
    });
}
