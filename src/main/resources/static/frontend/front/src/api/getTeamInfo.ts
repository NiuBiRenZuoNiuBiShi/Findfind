import { instance } from "./team.ts";
// 修改函数以接受一个参数 id
export const getTeamInfo = async (id: number): Promise<any> => {
    const token = localStorage.getItem('token');
    return await instance.get(`/teams/get/${id}`, { // 使用模板字符串插入id
        headers: {
            Authorization: token
        }
    });
};