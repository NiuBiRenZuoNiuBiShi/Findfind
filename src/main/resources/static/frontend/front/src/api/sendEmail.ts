import {instance} from "./user.ts";

export const sendEmailto = (email: any) => {
    const token = localStorage.getItem('token');
    return instance.post(`/user/sendEmail`, email, {
        headers:
            {
                Authorization: token
            }
    });
}