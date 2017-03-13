package dcastalia.com.munshijobsportal.Model;

/**
 * Created by imtiyaj-pc on 2/23/17.
 */
public class Agent {


    String id;
    String name;

    public Agent(String id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Agent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Agent() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
