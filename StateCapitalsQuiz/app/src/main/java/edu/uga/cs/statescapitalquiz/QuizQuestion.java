package edu.uga.cs.statescapitalquiz;

/**
 * This is a Quiz Question POJO class
 *
 * Tony Liang
 * Sadiq Salewala
 */

public class QuizQuestion {

    private int id;
    private String state;
    private String capital;
    private String city2;
    private String city3;
    private String answer;

    public QuizQuestion() {
        this.id = -1;
        this.state = "test";
        this.capital = "test";
        this.city2 = "test";
        this.city3 = "test";
        this.answer = "test";
    }

    public QuizQuestion(String state, String capital, String city2, String city3) {
        this.id = -1;
        this.state = state;
        this.capital = capital;
        this.city2 = city2;
        this.city3 = city3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCapital()
    {
        return capital;
    }

    public void setCapital(String capital)
    {
        this.capital = capital;
    }
    public String getCity2()
    {
        return city2;
    }

    public void setCity2(String city2)
    {
        this.city2 = city2;
    }
    public String getCity3()
    {
        return city3;
    }

    public void setCity3(String city3)
    {
        this.city3 = city3;
    }

    public String getAnswer()
    {
        return answer;
    }

    public void setAnswer(String answer)
    {
        this.answer = answer;
    }
}