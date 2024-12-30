import axios from 'axios';

//定义一个变量,记录公共的前缀  ,  baseURL
const baseURL = '/api';
export const instance = axios.create({baseURL})

export const createTeam = (team: any) => {
    const token = localStorage.getItem('token');
    return instance.post('/teams/create',
        team,
        {
            headers: {
                Authorization: token
            }
        });
}
export const updateTeam = (team: any) => {
    const token = localStorage.getItem('token');
    return instance.post('/teams/update',
        team,
        {
            headers: {
                Authorization: token
            }
        });
}
export const isTeamLeader = (teamId: any) => {
    const token = localStorage.getItem('token');
    return instance.get(`/teams/isTeamLeader/${teamId}`,
        {
            headers: {
                Authorization: token
            }
        });
}

export const deleteTeamById = (teamId: any) => {
    const token = localStorage.getItem('token');
    return instance.post(`/teams/delete/${teamId}`, {}, {
        headers:
            {
                Authorization: token
            }
    });
}
export const quitTeamById = (teamId: any) => {
    const token = localStorage.getItem('token');
    return instance.post(`/teams/quit/${teamId}`, {}, {
        headers: {
            Authorization: token
        }
    });
}
