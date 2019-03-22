package com.eat.today;

public class Phrase {
    private int phraseId;// phrase表的主键ID
    private String phraseDate;// 点赞的时间
    private String phraseName;// 点赞者的名字
    private int shuoId;// 点赞的说说id

    public int getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(int phraseId) {
        this.phraseId = phraseId;
    }

    public String getPhraseDate() {
        return phraseDate;
    }

    public void setPhraseDate(String phraseDate) {
        this.phraseDate = phraseDate;
    }

    public String getPhraseName() {
        return phraseName;
    }

    public void setPhraseName(String phraseName) {
        this.phraseName = phraseName;
    }

    public int getShuoId() {
        return shuoId;
    }

    public void setShuoId(int shuoId) {
        this.shuoId = shuoId;
    }
}
