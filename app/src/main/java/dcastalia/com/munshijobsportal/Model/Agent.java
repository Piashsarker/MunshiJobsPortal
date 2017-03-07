package dcastalia.com.munshijobsportal.Model;

/**
 * Created by imtiyaj-pc on 2/23/17.
 */
public class Agent {

    public Agent(int id,String name){
        this.id = id;
        this.name = name;
    }

    public Agent() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    int id;
    String name;
}
