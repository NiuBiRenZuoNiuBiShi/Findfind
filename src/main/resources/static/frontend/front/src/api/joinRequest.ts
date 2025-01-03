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
export const handleJoinRequest = (joinId: any, status: any, message: any) => {
    return instance.post(`/join/handle/${joinId}/${status}/${message}`, {}, {
        headers:
            {
                Authorization: token
            }
    });
}
export const deleteJoinRequest = (joinId: any) => {
    return instance.post(`/join/delete/${joinId}`, {}, {
        headers:
            {
                Authorization: token
            }
    });
}