package com.denihaxhiu.studentMVC.student;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    StudentRepository studentRepository;

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public void addStudent(Student student) {

        if(studentRepository.selectExistsEmail(student.getEmail())){
            throw new IllegalStateException("Error! Student email already exists");
        }

        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        if(!studentRepository.existsById(studentId)){
            throw new IllegalStateException("Student not found!");
        }
        studentRepository.deleteById(studentId);
    }

    public void updateStudent(Student student){
        if(!studentRepository.existsById(student.getId())){
            throw new IllegalStateException("Student now found!");
        }

        studentRepository.updateStudent(
                student.getName(),
                student.getEmail(),
                student.getGender(),
                student.getId()
        );

    }
}
