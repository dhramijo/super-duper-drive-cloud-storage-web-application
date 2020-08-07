package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MyBatis mapper for the File data model
 */
@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES")
    List<File> getFiles();

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File getFile(int fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{fileName}")
    File getFileByName(String fileName);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(File file);

    @Update("UPDATE FILES SET filename = #{filename}, contenttype = #{contentType}, filesize = #{fileSize}, userid = #{userId}, filedata = #{fileData} WHERE fileId = #{fileId}")
    void updateFile(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    void deleteFile(int fileId);

}
