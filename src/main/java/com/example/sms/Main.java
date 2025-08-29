package com.example.sms;

import com.example.sms.dao.StudentDAO;
import com.example.sms.model.Student;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String DB_FILE = "students.db";

    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO(DB_FILE);
        Scanner sc = new Scanner(System.in);
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> addStudent(sc, dao);
                    case "2" -> updateStudent(sc, dao);
                    case "3" -> deleteStudent(sc, dao);
                    case "4" -> searchStudent(sc, dao);
                    case "5" -> listAll(dao);
                    case "0" -> running = false;
                    default -> System.out.println("Invalid option. Try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        }
        sc.close();
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("=== Student Management System ===");
        System.out.println("1. Add student");
        System.out.println("2. Update student");
        System.out.println("3. Delete student");
        System.out.println("4. Search student by name");
        System.out.println("5. List all students");
        System.out.println("0. Exit");
    }

    private static void addStudent(Scanner sc, StudentDAO dao) {
        System.out.print("Name: ");
        String name = sc.nextLine().trim();
        System.out.print("Email: ");
        String email = sc.nextLine().trim();
        System.out.print("Phone: ");
        String phone = sc.nextLine().trim();
        Student s = new Student(name, email, phone);
        Student added = dao.addStudent(s);
        System.out.println("Added: " + added);
    }

    private static void updateStudent(Scanner sc, StudentDAO dao) {
        System.out.print("Enter student id to update: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        Student found = dao.getStudentById(id);
        if (found == null) { System.out.println("No student with id " + id); return; }
        System.out.println("Current: " + found);
        System.out.print("New name (leave blank to keep): ");
        String name = sc.nextLine().trim();
        if (!name.isEmpty()) found.setName(name);
        System.out.print("New email (leave blank to keep): ");
        String email = sc.nextLine().trim();
        if (!email.isEmpty()) found.setEmail(email);
        System.out.print("New phone (leave blank to keep): ");
        String phone = sc.nextLine().trim();
        if (!phone.isEmpty()) found.setPhone(phone);
        if (dao.updateStudent(found)) System.out.println("Updated."); else System.out.println("Update failed.");
    }

    private static void deleteStudent(Scanner sc, StudentDAO dao) {
        System.out.print("Enter student id to delete: ");
        int id = Integer.parseInt(sc.nextLine().trim());
        if (dao.deleteStudent(id)) System.out.println("Deleted student " + id);
        else System.out.println("No student with id " + id);
    }

    private static void searchStudent(Scanner sc, StudentDAO dao) {
        System.out.print("Enter name or fragment to search: ");
        String q = sc.nextLine().trim();
        List<Student> results = dao.searchByName(q);
        if (results.isEmpty()) System.out.println("No matches.");
        else results.forEach(System.out::println);
    }

    private static void listAll(StudentDAO dao) {
        List<Student> all = dao.getAll();
        if (all.isEmpty()) System.out.println("No students.");
        else all.forEach(System.out::println);
    }
}
