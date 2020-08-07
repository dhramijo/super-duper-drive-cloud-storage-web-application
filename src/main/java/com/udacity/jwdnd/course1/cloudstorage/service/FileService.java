package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> getFiles() {
        return fileMapper.getFiles();
    }

    public File getFile(int fileId) {
        return fileMapper.getFiles(fileId);
    }

    public File getFileByName(String fileName) {
        return fileMapper.getFileByName(fileName);
    }

    public int createFile(File file) {
        return fileMapper.insertFile(file);
    }

    public void updateFile(File file) {
        fileMapper.updateFile(file);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }
}
