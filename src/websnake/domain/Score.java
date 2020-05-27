package websnake.domain;

public class Score {
    private int id;
    private int userid;
    private int value;

    public Score(int id, int userid, int value) {
        this.id = id;
        this.userid = userid;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
