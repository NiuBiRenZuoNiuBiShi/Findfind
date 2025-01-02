import {instance} from "./user.ts";

const token = localStorage.getItem('token');

export const createJoinRequest = (teamId: any, userId: any, message: any) => {
    return instance.post(`/join/create/${teamId}/${userId}/${message}`, {}, {
        headers:
            {
                Authorization: token
            }
    });
}