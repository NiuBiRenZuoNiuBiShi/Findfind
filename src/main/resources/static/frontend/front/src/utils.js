// 读取默认头像并转换为Base64
const defaultAvatarFile = new File([''],'./assets/avatar.jpg', { type: 'image/jpg' });
const defaultAvatar = await readFileToBase64(defaultAvatarFile);
const uint8Array = base64ToUint8Array(defaultAvatar);

// src/utils.js
export function readFileToBase64(file) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = () => {
            // 移除数据 URL 的前缀
            const base64String = reader.result.split(',')[1];
            resolve(base64String);
        };
        reader.onerror = reject;
        reader.readAsDataURL(file);
    });
}
export function binaryStringToUint8Array(binaryString) {
    const len = binaryString.length;
    const bytes = new Uint8Array(len);
    for (let i = 0; i < len; i++) {
        bytes[i] = binaryString.charCodeAt(i);
    }
    return bytes;
}
export function base64ToUint8Array(base64String) {
    // 解码Base64字符串
    const binaryString = atob(base64String);
    // 将二进制字符串转换为Uint8Array
    const uint8Array = binaryStringToUint8Array(binaryString);
    return uint8Array;
}