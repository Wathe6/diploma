import React from 'react';
import { Table, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";

const StudentProfile = ({ data }) => {
    const { fio, subjects } = data.info;

    const uniqueSubjects = [...new Set(subjects.map(subject => subject.subject))];
    const subjectWorks = uniqueSubjects.map(subject => {
        const works = subjects.filter(s => s.subject === subject)
            .flatMap(s => s.work);
        return { subject, works: [...new Set(works)] };
    });

    const tableRows = subjects.map(subject => {
        const grades = subject.grade.map((grade, index) => ({
            work: subject.work[index],
            grade
        }));
        return { subject: subject.subject, grades };
    });

    return (
        <Table className="chakra-table">
            <Thead>
                <Tr>
                    <Th rowSpan="2" className="th">ФИО</Th>
                    {subjectWorks.map((subject, index) => (
                        <Th key={`subj-${index}`} colSpan={subject.works.length} className="th">{subject.subject}</Th>
                    ))}
                </Tr>
                <Tr>
                    {subjectWorks.flatMap(subject =>
                        subject.works.map((work, index) => (
                            <Th key={`${subject.subject}-work-${index}`} className="th">{work}</Th>
                        ))
                    )}
                </Tr>
            </Thead>
            <Tbody>
                <Tr>
                    <Td className="td">{fio}</Td>
                    {subjectWorks.flatMap(subject =>
                        subject.works.map((work, index) => {
                            const grade = tableRows
                                .find(row => row.subject === subject.subject)
                                ?.grades.find(g => g.work === work)?.grade || 0;
                            return <Td key={`${subject.subject}-${work}-${index}`} className="td">{grade}</Td>;
                        })
                    )}
                </Tr>
            </Tbody>
        </Table>
    );
};

export default StudentProfile;
