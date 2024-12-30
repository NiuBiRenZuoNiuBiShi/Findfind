import { instance } from "./user";
export const updateInvite = async (requestId: number, status: number, response: string): Promise<any> => {
    const token = localStorage.getItem('token');
    try {
        return await instance.post('/invite/handle', {
        }, {
            params: {
                requestId:requestId,
                status:status,
                response:response
            },
            headers: {
                Authorization: token
            }
        });
    } catch (error) {
        console.error('Error updating invite:', error);
        throw error; // 重新抛出错误，以便调用者可以处理
    }
};