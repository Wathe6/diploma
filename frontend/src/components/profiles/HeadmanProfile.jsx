import React from 'react';
import { Table, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";

const HeadmanProfile = ({ data }) => {
    const { works, grades } = data.info;

    // Получаем список студентов
    const students = Object.keys(grades);

    // Получаем уникальные предметы и работы
    const subjects = Object.keys(works);

    return (
        <Table className="chakra-table">
            <Thead>
                <Tr>
                    <Th rowSpan="2" className="th">ФИО</Th>
                    {subjects.map((subject, index) => (
                        <Th key={`subj-${index}`} colSpan={works[subject].length} className="th">{subject}</Th>
                    ))}
                </Tr>
                <Tr>
                    {subjects.flatMap(subject =>
                        works[subject].map((work, index) => (
                            <Th key={`${subject}-work-${index}`} className="th">{work}</Th>
                        ))
                    )}
                </Tr>
            </Thead>
            <Tbody>
                {students.map((student, index) => (
                    <Tr key={`student-${index}`}>
                        <Td className="td">{student}</Td>
                        {subjects.flatMap(subject =>
                            works[subject].map((work, workIndex) => {
                                const grade = grades[student][subject][workIndex] || 0;
                                return <Td key={`${student}-${subject}-${workIndex}`} className="td">{grade}</Td>;
                            })
                        )}
                    </Tr>
                ))}
            </Tbody>
        </Table>
    );
};

export default HeadmanProfile;