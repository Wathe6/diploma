import React, { useState } from 'react';
import { Box, Select, Table, Tbody, Td, Th, Thead, Tr } from "@chakra-ui/react";

const HeadProfile = ({ data }) => {
    const [selectedGroup, setSelectedGroup] = useState(Object.keys(data.info)[0]);

    const handleGroupChange = (event) => {
        setSelectedGroup(event.target.value);
    };

    const selectedData = data.info[selectedGroup];
    const { works, grades } = selectedData;

    // Получаем список студентов
    const students = Object.keys(grades);

    // Получаем уникальные предметы и работы
    const subjects = Object.keys(works);

    return (
        <Box>
            <Select onChange={handleGroupChange} value={selectedGroup} mb={4} style={{fontSize: "28px"}}>
                {Object.keys(data.info).map((group, index) => (
                    <option key={index} value={group} style={{fontSize: "28px"}}>{group}</option>
                ))}
            </Select>
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
        </Box>
    );
};

export default HeadProfile;
