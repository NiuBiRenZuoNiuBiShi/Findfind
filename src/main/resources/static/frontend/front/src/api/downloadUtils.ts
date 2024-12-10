export const downloadUtils = {
    /**
     * 将二进制数据生成可下载的 Blob URL
     * @param {Uint8Array|ArrayBuffer} data 二进制数据
     * @param {string} fileName 文件名
     */
    generateDownloadLink(data, fileName) {
        // 创建 Blob 对象
        const blob = new Blob([data], { type: "application/octet-stream" });
        // 创建临时 URL
        const url = URL.createObjectURL(blob);

        // 创建下载链接并触发下载
        const anchor = document.createElement("a");
        anchor.href = url;
        anchor.download = fileName;
        document.body.appendChild(anchor); // 必要时添加到 DOM
        anchor.click();
        document.body.removeChild(anchor); // 下载后移除节点

        // 延迟释放 URL 防止下载中断
        setTimeout(() => {
            URL.revokeObjectURL(url);
        }, 100);
    },
};
