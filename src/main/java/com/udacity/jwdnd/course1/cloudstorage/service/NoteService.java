package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public int createNote(Note note) {
        return noteMapper.insertNote(note);
    }

    public List<Note> getNotes() {
        return noteMapper.getNotes();
    }

    public Note getNote(int noteId) {
        return noteMapper.getNote(noteId);
    }

    public void updateNote(Note note) {
        noteMapper.insertNote(note);
    }

    public void deleteNote(int noteId) {
        noteMapper.deleteNote(noteId);
    }
}
