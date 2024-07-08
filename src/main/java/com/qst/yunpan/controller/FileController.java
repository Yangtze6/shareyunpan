package com.qst.yunpan.controller;


import com.qst.yunpan.pojo.FileCustom;
import com.qst.yunpan.pojo.RecycleFile;
import com.qst.yunpan.pojo.Result;
import com.qst.yunpan.pojo.SummaryFile;
import com.qst.yunpan.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private FileService fileService;
    /**
     * 获取文件列表
     *
     * @param path
     *            路径
     * @return Json对象
     */
    @RequestMapping("/getFiles")
    public @ResponseBody
    Result<List<FileCustom>> getFiles(String path) {
        //根据项目路径及用户名、文件名获取上传文件的真实路径
        String realPath = fileService.getFileName(request, path);
        //获取路径下所有的文件信息
        List<FileCustom> listFile = fileService.listFile(realPath);
        //将文件信息封装成Json对象
        Result<List<FileCustom>> result = new Result<List<FileCustom>>(325,
                true, "获取成功");
        result.setData(listFile);
        return result;
    }

    @RequestMapping("/upload")
    public @ResponseBody Result<String> upload(
            @RequestParam("files") MultipartFile[] files, String currentPath) {
        try {
            fileService.uploadFilePath(request, files, currentPath);
        } catch (Exception e) {
            return new Result<>(301, false, "上传失败");
        }
        return new Result<String>(305, true, "上传成功");
    }

    @RequestMapping("/download")
    public ResponseEntity<byte[]> download(String currentPath, String[] downPath, String username) {
        try {
            String down = request.getParameter("downPath");
            File downloadFile = fileService.downPackage(request, currentPath,downPath, username);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            String fileName = new String(downloadFile.getName().getBytes("utf-8"), "iso-8859-1");
            headers.setContentDispositionFormData("attachment", fileName);
            byte[] fileToByteArray = org.apache.commons.io.FileUtils.readFileToByteArray(downloadFile);
            fileService.deleteDownPackage(downloadFile);
            return new ResponseEntity<byte[]>(fileToByteArray, headers,    HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查找文件（模糊查询）
     *
     * @param currentPath
     *            当面路径
     * @param regType
     *            查找文件类型
     * @return Json对象
     */
    /*@RequestMapping("/searchFile")
    public @ResponseBody Result<List<FileCustom>> searchFile(String currentPath, String regType) {
        try {
            List<FileCustom> searchFile = fileService.searchFile(request, currentPath, regType);
            Result<List<FileCustom>> result = new Result<>(376, true, "查找成功");
            result.setData(searchFile);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(371, false, "查找失败");
        }
    }*/

    /**
     * 新建文件夹
     *
     * @param currentPath
     *            当前路径
     * @param directoryName
     *            文件夹名
     * @return Json对象
     */
    @RequestMapping("/addDirectory")
    public @ResponseBody Result<String> addDirectory(String currentPath, String directoryName) {
        try {
            fileService.addDirectory(request, currentPath, directoryName);
            return new Result<>(336, true, "添加成功");
        } catch (Exception e) {
            return new Result<>(331, false, "添加失败");
        }
    }
    /**
     * 删除文件夹
     *
     * @param currentPath
     *            当前路径
     * @param directoryName
     *            文件夹名
     * @return Json对象
     */
    @RequestMapping("/delDirectory")
    public @ResponseBody Result<String> delDirectory(String currentPath,String[] directoryName) {
        try {
            fileService.delDirectory(request, currentPath, directoryName);
            return new Result<>(346, true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(341, false, "删除失败");
        }
    }
    /**
     * 重命名文件夹
     *
     * @param currentPath
     *            当前路径
     * @param srcName
     *            源文件名
     * @param destName
     *            目标文件名
     * @return Json对象
     */
    @RequestMapping("/renameDirectory")
    public @ResponseBody Result<String> renameDirectory(String currentPath,    String srcName, String destName) {
        try {
            fileService.renameDirectory(request, currentPath, srcName, destName);
            return new Result<>(356, true, "重命名成功");
        } catch (Exception e) {
            return new Result<>(351, false, "重命名失败");
        }
    }
    /**
     * 用于获取该用户的所有目录结构并返回前台
     *
     *
     */

    @RequestMapping("/summarylist")
    public String summarylist(Model model) {
        String webrootpath = fileService.getFileName(request, "");
        int number = webrootpath.length();
        SummaryFile rootlist = fileService.summarylistFile(webrootpath, number);
        model.addAttribute("rootlist", rootlist);
        return "summarylist";
    }
    /**
     * 用于对文件进行复制
     *
     *
     */
    @RequestMapping("/copyDirectory")
    public @ResponseBody Result<String> copyDirectory(String currentPath,String[] directoryName, String targetdirectorypath) throws Exception {
        try {
            fileService.copyDirectory(request, currentPath, directoryName,
                    targetdirectorypath);
            return new Result<>(366, true, "复制成功");
        } catch (IOException e) {
            return new Result<>(361, true, "复制失败");
        }
    }
    /**
     * 移动文件夹
     *
     * @param currentPath
     *            当前路径
     * @param directoryName
     *            文件夹名
     * @param targetdirectorypath
     *            目标位置
     * @return Json对象
     */
    @RequestMapping("/moveDirectory")
    public @ResponseBody Result<String> moveDirectory(String currentPath,String[] directoryName, String targetdirectorypath) {
        try {
            fileService.moveDirectory(request, currentPath, directoryName,targetdirectorypath);
            return new Result<>(366, true, "移动成功");
        } catch (Exception e) {
            return new Result<>(361, true, "移动失败");
        }
    }
    /**
     * 查找文件（模糊查询）
     *
     * @param reg
     *            要查找的文件名
     * @param currentPath
     *            当面路径
     * @param regType
     *            查找文件类型
     * @return Json对象
     */
   @RequestMapping("/searchFile")
    public @ResponseBody Result<List<FileCustom>> searchFile(String reg,String currentPath, String regType) {
        try {
            List<FileCustom> searchFile = fileService.searchFile(request,
                    currentPath, reg, regType);
            Result<List<FileCustom>> result = new Result<>(376, true, "查找成功");
            result.setData(searchFile);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(371, false, "查找失败");
        }
    }

    /**
     * 用于获取当前用户回收站下的文件信息；当用户删除过文件时则返回findDelFile，提示用户回收站不为空
     *
     */
    @RequestMapping("/recycleFile")
    public String recycleFile() {
        try {
            List<RecycleFile> findDelFile = fileService.recycleFiles(request);
            if(null != findDelFile && findDelFile.size() != 0) {
                request.setAttribute("findDelFile", findDelFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "recycle";
    }

    /*
     * --还原回收站文件-- --获取目的路径以及文件名--
     */
    @RequestMapping("/revertDirectory")
    public @ResponseBody Result<String> revertDirectory(int[] fileId) {
        try {
            fileService.revertDirectory(request, fileId);
            return new Result<>(327, true, "还原成功");
        } catch (Exception e) {
            return new Result<>(322, false, "还原失败");
        }
    }
    /* --清空回收站-- */
    @RequestMapping("/delAllRecycle")
    public @ResponseBody Result<String> delAllRecycleDirectory() {
        try {
            fileService.delAllRecycle(request);
            // 返回状态码
            return new Result<>(327, true, "删除成功");
        } catch (Exception e) {
            return new Result<>(322, false, "删除失败");
        }
    }

    /*
     * 接受和处理在线图片/txt预览功能
     */
    @RequestMapping("/openFile")
    public void openFile(HttpServletResponse response, String currentPath, String fileName, String fileType) {
        try {
            fileService.respFile(response, request, currentPath, fileName, fileType);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}