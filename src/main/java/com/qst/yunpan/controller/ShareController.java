package com.qst.yunpan.controller;

import com.qst.yunpan.pojo.Result;
import com.qst.yunpan.pojo.ShareFile;
import com.qst.yunpan.service.ShareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShareController {
    @Autowired
    private ShareService shareService;

    /*
     * 创建文件分享控制层
     *
     */
    @RequestMapping("/shareFile")
    public @ResponseBody Result<String> shareFile(HttpServletRequest request, String currentPath, String[] shareFile){
        try {
            String shareUrl = shareService.shareFile(request, currentPath, shareFile);
            Result<String> result = new Result<>(405, true, "分享成功");
            result.setData(shareUrl);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(401, false, "分享失败");
        }
    }
    /*
     * 用于接收分享访问的share方法
     *
     */
    @RequestMapping("/share")
    public String share(HttpServletRequest request, String shareUrl){
        try {
            List<ShareFile> files = shareService.findShare(request, shareUrl);
            request.setAttribute("files", files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "share";
    }
    /**
     * 按状态查找分享
     * @param request
     * @param status    状态 1|2|-1
     * @return
     */
    @RequestMapping("/searchShare")
    public @ResponseBody Result<List<ShareFile>> searchShare(HttpServletRequest request, int status){
        try {
            List<ShareFile> files = shareService.findShareByName(request, status);
            Result<List<ShareFile>> result = new Result<>(415, true, "获取成功");
            result.setData(files);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(411, false, "获取失败");
        }
    }
    /*
     * 用于取消分享
     *
     */
    @RequestMapping("/cancelShare")
    public @ResponseBody Result<String> cancelShare(String url, String filePath, int status){
        try {
            String msg = shareService.cancelShare(url, filePath, status);
            Result<String> result = new Result<String>(425, true, msg);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<String>(421, false, "删除失败");
        }
    }


}