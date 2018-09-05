package com.pritesh.notes;

public class Note {
    private String created;
    private String title;
    private String description;

    public Note(String created, String title, String description) {
        this.created = created;
        this.title = title;
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
