package com.galileo.sda.savingdataapp.database;

public class NoteModel {
    private long id;
    private String title;
    private String content;
    private String datetime;

    public NoteModel(){}
    public NoteModel(String title, String content, String datetime){
        this.title=title;
        this.content=content;
        this.datetime=datetime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String toString(){
        return "Title: "+this.getTitle()+"\nTime: "+this.getDatetime()+"\nContent: "+this.getContent()+"\n";
    }
}
