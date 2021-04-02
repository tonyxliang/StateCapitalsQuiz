package edu.uga.cs.statescapitalquiz;

/**
 * This is a Quiz POJO class
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class Quiz {

    private int id;
    private String date;
    private int score;

    public Quiz() {
        id = 0;
        date = "";
        score = 0;
    }

    public Quiz(String date, int score) {
        this.date = date;
        this.score = score;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {

        this.id = id;
    }

    public String getDate()
    {

        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public int getScore()
    {
        return score;
    }

    public void setScore(int score)
    {
        this.score = score;
    }
}
