package People;

abstract class Person {
    private String name;
    private String mail;
    private String phoneNumber;
    private String gender;
    private int age;
    private String department;

    public Person(String name, String mail, String phoneNumber, String gender, int age, String department) {
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.age = age;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
 
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
