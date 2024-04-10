import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MultiThreadStudentProcessor {

    public static void main(String[] args) {
        final List<Student> students = new ArrayList<>();
        final CountDownLatch latch = new CountDownLatch(1);
        final boolean[] isDigitPrime = new boolean[1];

        Thread thread1 = new Thread(() -> {
            students.addAll(parseStudentFromXML("C:\\Users\\admin\\Desktop\\JAVA.INTELIJI\\BTgiuaky2\\ThiGK2JavaNangcao-23NS016-nguyenthanhdat\\src\\Infostudent.xml"));
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            try {
                latch.await();
                students.forEach(student -> {
                    int age = student.tinhTuoi();
                    String mahoaTuoi = student.mahoaTuoi(age);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread3 = new Thread(() -> {
            try {
                latch.await();
                students.forEach(student -> {
                    LocalDate dob = student.getDob();
                    int sumDigits = sumDigits(dob.getDayOfMonth()) + sumDigits(dob.getMonthValue()) + sumDigits(dob.getYear());
                    isDigitPrime[0] = Student.ktrsonguyento(sumDigits);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread1.start();
        thread2.start();
        thread3.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            students.forEach(student -> writeXML(student, isDigitPrime[0]));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static List<Student> parseStudentFromXML(String filePath) {
        List<Student> students = new ArrayList<>();
        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("Student");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Element eElement = (Element) nList.item(temp);
                String id = eElement.getElementsByTagName("Id").item(0).getTextContent();
                String name = eElement.getElementsByTagName("Name").item(0).getTextContent();
                String address = eElement.getElementsByTagName("Address").item(0).getTextContent();
                String dob = eElement.getElementsByTagName("DateOfBirth").item(0).getTextContent();
                LocalDate dateOfBirth = LocalDate.parse(dob, DateTimeFormatter.ISO_LOCAL_DATE);
                students.add(new Student(id, name, address, dateOfBirth));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return students;
    }

    private static int sumDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number /= 10;
        }
        return sum;
    }

    private static void writeXML(Student student, boolean isDigitPrime) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            Element rootElement = doc.createElement("class");
            doc.appendChild(rootElement);
            Element studentElement = doc.createElement("student");
            rootElement.appendChild(studentElement);
            studentElement.setAttribute("id", student.getId());
            Element nameElement = doc.createElement("name");
            nameElement.appendChild(doc.createTextNode(student.getName()));
            studentElement.appendChild(nameElement);
            Element addressElement = doc.createElement("address");
            addressElement.appendChild(doc.createTextNode(student.getAddress()));
            studentElement.appendChild(addressElement);
            Element dobElement = doc.createElement("dob");
            dobElement.appendChild(doc.createTextNode(student.getDob().toString()));
            studentElement.appendChild(dobElement);
            Element ageElement = doc.createElement("age");
            ageElement.appendChild(doc.createTextNode(String.valueOf(student.tinhTuoi())));
            studentElement.appendChild(ageElement);
            Element encodedAgeElement = doc.createElement("encodedAge");
            encodedAgeElement.appendChild(doc.createTextNode(student.mahoaTuoi(student.tinhTuoi())));
            studentElement.appendChild(encodedAgeElement);
            Element isDigitPrimeElement = doc.createElement("isDigitPrime");
            isDigitPrimeElement.appendChild(doc.createTextNode(String.valueOf(isDigitPrime)));
            studentElement.appendChild(isDigitPrimeElement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\Users\\admin\\Desktop\\JAVA.INTELIJI\\BTgiuaky2\\ThiGK2JavaNangcao-23NS016-nguyenthanhdat\\src\\kq.xml"));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
