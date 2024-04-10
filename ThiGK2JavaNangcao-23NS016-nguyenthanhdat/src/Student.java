import java.time.LocalDate;
import java.time.Period;

public class Student {
    private String id;
    private String name;
    private String address;
    private LocalDate dob;

    public Student(String id, String name, String address, LocalDate dob) {
        if (id == null || name == null || address == null || dob == null) {
            throw new IllegalArgumentException("chua nhap thuoc tinh ");
        }
        this.id = id;
        this.name = name;
        this.address = address;
        this.dob = dob;
    }

    public int tinhTuoi() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

    public String mahoaTuoi(int age) {
        return new StringBuilder().append(age).reverse().toString();
    }

    public static boolean ktrsonguyento(int number) {
        if (number <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
