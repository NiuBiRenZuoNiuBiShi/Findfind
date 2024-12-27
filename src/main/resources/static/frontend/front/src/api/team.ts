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