package com.cmdesign.hellonwsuaf.adapter;

/**
 * Created by Administrator on 2015/10/21.
 */
public class Score {
    private String ScoreName=null;
    private String point=null;
    private String testScore=null;
    private String type=null;
    private String examScore=null;
    private String credit=null;
    private String xuefen=null;
    private String isright=null;
    private String Id=null;
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public String getExamScore() {
        return examScore;
    }

    public void setExamScore(String examScore) {
        this.examScore = examScore;
    }


    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getScoreName() {
        return ScoreName;
    }

    public void setScoreName(String scoreName) {
        ScoreName = scoreName;
    }

    public String getTestScore() {
        return testScore;
    }

    public void setTestScore(String testScore) {
        this.testScore = testScore;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getIsright() {
        return isright;
    }

    public void setIsright(String isright) {
        this.isright = isright;
    }

    @Override
    public String toString() {
        return getTestScore();
    }
}
