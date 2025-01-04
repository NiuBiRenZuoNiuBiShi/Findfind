import {instance} from "./user";

export const getUserJoins = async (page: number, size: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/user/joins', {
        params: {
            page: page,
            size: size
        },
        headers: {
            Authorization: token
        }
    });
};

export const updateUserJoin = async (joinId: number) => {
    const token = localStorage.getItem('token');
    return await instance.post(`/join/update/${joinId}/${1}/${"已通过其他方式加入"}`, {}, {
        headers: {
            Authorization: token
        }
    });
}
export const getOtherUserJoins = async (page: number, size: number, teamId: any): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/join/joins', {
        params: {
            page: page,
            size: size,
            teamId: teamId
        },
        headers: {
            Authorization: token
        }
    });
}
export const getAllOtherUserJoins = async (page: number, size: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get('/join/joins/all', {
        params: {
            page: page,
            size: size
        },
        headers: {
            Authorization: token
        }
    });
}