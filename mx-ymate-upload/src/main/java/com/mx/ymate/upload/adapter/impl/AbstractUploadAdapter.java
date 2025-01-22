package com.mx.ymate.upload.adapter.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mx.ymate.dev.support.mvc.MxResult;
import com.mx.ymate.upload.IMxUploadConfig;
import com.mx.ymate.upload.MxUpload;
import com.mx.ymate.upload.adapter.IUploadAdapter;
import com.mx.ymate.upload.bean.FileInfo;
import com.mx.ymate.upload.bean.Upload;
import net.coobird.thumbnailator.Thumbnails;
import net.ymate.platform.commons.util.DateTimeUtils;
import net.ymate.platform.commons.util.FileUtils;
import net.ymate.platform.commons.util.ImageUtils;
import net.ymate.platform.commons.util.MimeTypeUtils;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.charset.Charset;

import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_CONTENT_TYPE_ERROR;
import static com.mx.ymate.upload.code.MxUploadCode.MX_UPLOAD_SIZE_ERROR;


/**
 * @Author: mengxiang.
 * @Date 2024/11/1.
 * @Time: 09:36.
 * @Description: 文件存储抽象类
 */
public abstract class AbstractUploadAdapter implements IUploadAdapter {

    protected IMxUploadConfig config;


    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public MxResult upload(File file) throws Exception {
        if (config == null) {
            config = MxUpload.get().getConfig();
        }
        MxResult mxResult = checkConfig();
        if (!mxResult.isSuccess()) {
            return mxResult;
        }
        //文件信息
        FileInfo fileInfo = getFileInfo(file);
        //文件太大
        if (fileInfo.getFileSize() > config.maxSize()) {
            return MxResult.create(MX_UPLOAD_SIZE_ERROR);
        }
        //文件类型不对
        if (CollUtil.isNotEmpty(config.contentTypeList()) && !config.contentTypeList().contains(fileInfo.getType())) {
            return MxResult.create(MX_UPLOAD_CONTENT_TYPE_ERROR);
        }
        Upload upload = fileInfo.getUpload();
        if (upload != null) {
            return MxResult.okData(upload);
        }
        //保存文件
        mxResult = saveFile(file, fileInfo);
        if (!mxResult.isSuccess()) {
            return mxResult;
        }
        //生成文件访问的url
        String newFileName = fileInfo.getNewFileName();
        String showUrl = getShowUrl() + newFileName;
        String thumbUrl = null;
        //生成缩略图
        if (config.isCreateThumb()) {
            //生成并且保存缩略图
            //缩略图的文件位置
            String hash = fileInfo.getHash();
            //缩略图文件名
            String thumbFileName = newFileName.replace(hash + ".", hash + "_thumb.");
            Thumbnails.Builder<BufferedImage> thumbFile = createThumb(file, fileInfo.getType(),fileInfo.getExtName());
            //返回缩略图url 可以为空
            if (thumbFile != null) {
                //创建一个临时文件 把缩略图暂时保存一下  然后在调用保存方法去保存
                File tempFile = FileUtils.createTempFile("mxUpload",thumbFileName);
                thumbFile.toFile(tempFile);
                //保存缩略图
                FileInfo thumbFileInfo = getFileInfo(tempFile);
                thumbFileInfo.setNewFileName(thumbFileName);
                thumbFileInfo.setType(fileInfo.getType());
                saveFile(tempFile, thumbFileInfo);
                thumbUrl = getShowUrl() + thumbFileName;
                //上传成功之后 把临时文件删除了
                FileUtil.del(tempFile);
            }
        }
        //生成并且缓存json数据文件
        upload = cacheUpload(fileInfo, showUrl, thumbUrl);
        return MxResult.okData(upload);
    }

    /**
     * 检查配置
     *
     * @return
     */
    protected abstract MxResult checkConfig();

    /**
     * 保存文件
     *
     * @param srcFile
     * @param fileInfo
     * @return
     * @throws Exception
     */
    protected abstract MxResult saveFile(File srcFile, FileInfo fileInfo);

    /**
     * 获取显示url
     * @return
     */
    protected abstract String getShowUrl();

    protected String defaultShowUrl(){
        String showUrl = config.showUrl();
        if(StringUtils.isNotBlank(config.prefix())){
            showUrl = showUrl + config.prefix();
        }
        return showUrl;
    }

    /**
     * 获取是否上传过 返回空没上传或者没配置  否则返回上传信息
     *
     * @param type
     * @param hash
     * @return
     */
    protected Upload getUpload(String type, String hash) {
        String jsonFilePath = config.jsonFilePath();
        if (StringUtils.isBlank(jsonFilePath)) {
            return null;
        }
        File hashFile = new File(jsonFilePath + File.separator + type, hash + ".json");
        if (!hashFile.exists()) {
            return null;
        }
        JSONObject json = JSONUtil.readJSONObject(hashFile, Charset.defaultCharset());
        if (json == null) {
            return null;
        }
        return new Upload(hash, json.getStr("name"), json.getStr("extension"),
                json.getStr("url"), json.getStr("thumbUrl"), json.getLong("size"),
                json.getStr("type"), false, json.getLong("createTime"));
    }

    /**
     * 缓存数据文件
     *
     * @param fileInfo
     */
    protected Upload cacheUpload(FileInfo fileInfo, String url, String thumbUrl) {
        Upload upload = new Upload(fileInfo.getHash(), fileInfo.getFileName(), fileInfo.getExtName(), url, thumbUrl, fileInfo.getFileSize(), fileInfo.getType(), true, System.currentTimeMillis());
        String jsonFilePath = config.jsonFilePath();
        if (StringUtils.isBlank(jsonFilePath)) {
            return upload;
        }
        //有文件就不存了
        File hashFile = new File(jsonFilePath + File.separator + upload.getType(), upload.getHash() + ".json");
        if (hashFile.exists()) {
            upload.setNew(false);
            return upload;
        }
        FileUtil.writeString(JSONUtil.toJsonStr(upload), hashFile, Charset.defaultCharset());
        return upload;
    }

    /**
     * 获取新的文件名称 带目录的
     *
     * @param type
     * @param hash
     * @param extName
     * @return
     */
    protected String getNewFileName(String type, String hash, String extName) {
        //路径格式 例如 image/20210/01/01/hash.png
        long time = System.currentTimeMillis();
        String year = DateTimeUtils.formatTime(time, "yyyy");
        String month = DateTimeUtils.formatTime(time, "MM");
        String day = DateTimeUtils.formatTime(time, "dd");
        return StrUtil.format("{}/{}/{}/{}/{}.{}", type, year, month, day, hash, extName);
    }

    /**
     * 获取文件信息
     *
     * @param file
     * @return
     * @throws Exception
     */
    private FileInfo getFileInfo(File file) throws Exception {
        //文件hash
        String hash = FileUtils.getHash(file);
        //文件名称
        String fileName = file.getName();
        //获取扩展名
        String extName = FileUtils.getExtName(fileName);
        //文件类型
        String type = MimeTypeUtils.getFileMimeType(extName);
        //数据文件
        Upload upload = getUpload(type, hash);
        //新文件名
        String newFileName = getNewFileName(type, hash, extName);
        return new FileInfo(hash, file.length(), fileName, extName, type, upload, newFileName, FileUtil.getInputStream(file));
    }

    protected Thumbnails.Builder<BufferedImage> createThumb(File file,String type, String extName) throws Exception {
        if (!config.isCreateThumb()) {
            return null;
        }
        String resourceType =  StringUtils.substringBefore(type,  "/").toUpperCase();
        //不是图片直接return
        if(!"IMAGE".equals(resourceType)){
            return null;
        }
        int thumbWidth = Math.max(config.thumbWidth(), 0);
        int thumbHeight = Math.max(config.thumbHeight(), 0);
        //有一个设置是0 或者 不设置 就不生成
        if (thumbWidth == 0 || thumbHeight == 0) {
            return null;
        }
        BufferedImage srcFileImage = ImageIO.read(file);
        if (srcFileImage == null) {
            return null;
        }
        int originWidth = srcFileImage.getWidth();
        int originHeight = srcFileImage.getHeight();
        // 原图的宽和高 有一个小于 缩略图的 就不生成
        if (originWidth < thumbWidth || originHeight < thumbHeight) {
            return null;
        }
        //生成缩略图
        return ImageUtils.resize(srcFileImage, thumbWidth, thumbHeight, config.thumbQuality(), extName);

    }
}
