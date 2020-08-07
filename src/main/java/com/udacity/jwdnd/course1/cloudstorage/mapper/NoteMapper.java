package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * MyBatis mapper for the Note data model
 */
@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES")
    List<Note> getNotes();

    @Select("SELECT * FROM USERS WHERE noteid = #{noteId}")
    Note getNote(int noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle = #{noteTitle}, notedescription = #{noteDescription}, userid = #{userId} WHERE noteid = #{noteId}")
    void updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId}")
    void deleteNote(int noteId);
}
