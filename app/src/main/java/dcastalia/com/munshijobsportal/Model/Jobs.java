package dcastalia.com.munshijobsportal.Model;

/**
 * Created by shahimtiyaj on 12/23/2016.
 */

public class Jobs {
    private String company;
    private String jobId;
    private String available_job;
    private String job_title;
    private String job_position;
    private String country;
    private String date;
    private String vacancy;
    private String experince;
    private String salary;
    private String age;
    private String gender;
    private String jobNature;
    private String jobDescription;
    private String jobRequirement;
    public Jobs(String company, String vacancy, String available_job, String job_title, String job_position, String country, String date, String salary, String experince, String age, String jobNature, String gender, String jobDescription, String jobRequirement) {
        this.company = company;
        this.vacancy = vacancy;
        this.available_job = available_job;
        this.job_title = job_title;
        this.job_position = job_position;
        this.country = country;
        this.date = date;
        this.salary = salary;
        this.experince = experince;
        this.age = age;
        this.jobNature = jobNature;
        this.gender = gender;
        this.jobDescription = jobDescription;
        this.jobRequirement = jobRequirement;
    }
    public Jobs() {

    }

    public Jobs(String available_job, String job_title, String job_position, String country, String date) {
        this.available_job = available_job;
        this.job_title = job_title;
        this.job_position = job_position;
        this.country = country;
        this.date = date;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getExperince() {
        return experince;
    }

    public void setExperince(String experince) {
        this.experince = experince;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobNature() {
        return jobNature;
    }

    public void setJobNature(String jobNature) {
        this.jobNature = jobNature;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobRequirement() {
        return jobRequirement;
    }

    public void setJobRequirement(String jobRequirement) {
        this.jobRequirement = jobRequirement;
    }

    public String getJob_position() {
        return job_position;
    }

    public void setJob_position(String job_position) {
        this.job_position = job_position;
    }

    public String getAvailable_job() {
        return available_job;
    }

    public void setAvailable_job(String available_job) {
        this.available_job = available_job;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
