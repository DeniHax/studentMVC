package com.denihaxhiu.studentMVC.student;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canFindAllStudents() {
        //when
        underTest.findAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student (
                "Jane Doe",
                "janedoe@gmail.com",
                Gender.FEMALE
        );
        //when
        underTest.addStudent(student);

        //then
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);

        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrowWhenEmailIsTaken() {
        //given
        Student student = new Student (
                "Jane Doe",
                "janedoe@gmail.com",
                Gender.FEMALE
        );

        given(studentRepository.selectExistsEmail(student.getEmail()))
                .willReturn(true);

        //then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Error! Student email already exists");

        verify(studentRepository, never()).save(any());

    }

    @Test
    void canDeleteStudent() {
        //given
        long id = 20;
        given(studentRepository.existsById(id))
                .willReturn(true);

        //when
        underTest.deleteStudent(id);

        //then
        verify(studentRepository).deleteById(id);

    }

    @Test
    void willThrowWhenDeleteStudentIsNotFound() {
        //given
        long id = 20;
        given(studentRepository.existsById(id))
                .willReturn(false);

        //when
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Student not found!");

        //then
        verify(studentRepository, never()).deleteById(any());

    }

    @Test
    void canUpdateStudent() {

        //given
        Student student = new Student (
                "Jane Doe",
                "janedoe@gmail.com",
                Gender.FEMALE
        );

        underTest.addStudent(student);

        Student updateStudent = new Student (
                "John Doe",
                "johndoe@gmail.com",
                Gender.MALE
        );
        given(studentRepository.existsById(student.getId()))
                .willReturn(true);

        //when
        underTest.updateStudent(updateStudent);

        //then
        verify(studentRepository).updateStudent(
                updateStudent.getName(),
                updateStudent.getEmail(),
                updateStudent.getGender(),
                student.getId());

    }

    @Test
    void willThrowWhenUpdateStudentIsNotFound() {
        //given
        long id = 20;
        given(studentRepository.existsById(id))
                .willReturn(false);

        //when
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Student not found!");

        //then
        verify(studentRepository, never()).updateStudent(
                any(),
                any(),
                any(),
                any());

    }

}